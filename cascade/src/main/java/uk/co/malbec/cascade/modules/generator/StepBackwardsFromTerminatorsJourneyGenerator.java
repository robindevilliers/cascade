package uk.co.malbec.cascade.modules.generator;


import uk.co.malbec.cascade.annotations.OnlyRunWith;
import uk.co.malbec.cascade.annotations.ReEntrantTerminator;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Terminator;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.JourneyGenerator;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.sort;
import static uk.co.malbec.cascade.utils.ReflectionUtils.getValueOfFieldAnnotatedWith;
import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class StepBackwardsFromTerminatorsJourneyGenerator implements JourneyGenerator {

    private ConditionalLogic conditionalLogic;

    public StepBackwardsFromTerminatorsJourneyGenerator(ConditionalLogic conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    public List<Journey> generateJourneys(List<Class> allScenarios, Class<?> controlClass, Filter filter) {

        //sort the scenarios so that the generation of journeys is always deterministic from the users point of view.
        sort(allScenarios, new ClassComparator());

        OnlyRunWithFilter onlyRunWithFilter = new OnlyRunWithFilter(conditionalLogic);
        UnusedScenariosFilter unusedScenariosFilter = new UnusedScenariosFilter(allScenarios);
        RedundantFilter redundantFilter = new RedundantFilter();
        Filter compositeFilter = new CompositeFilter(onlyRunWithFilter, unusedScenariosFilter, filter, redundantFilter);

        List<Class> terminators = new ArrayList<Class>();

        //these are scenarios marked with a Terminator annotation - explicit terminators.
        findTerminatngScenarios(allScenarios, terminators);

        //these are scenarios marked with a ReEntrantTerminator annotation - these are terminators but only after already being in the journey n number of times.
        findReEntrantTerminatngScenarios(allScenarios, terminators);

        //these are scenarios that belong to steps that are not followed by other steps - implicit terminators.
        findDanglingScenarios(allScenarios, terminators);

        //sort terminators so that we always generate the same journeys.  This guarantees that we always have the same number of tests in the first pass.
        sort(terminators, new ClassComparator());

        List<Journey> journeys = new ArrayList<Journey>();
        for (Class terminator : terminators) {
            generatingTrail(terminator, new ArrayList<Class>(), allScenarios, journeys, controlClass, compositeFilter);
        }

        //go through all scenarios and find scenarios that are not in any journeys and generate an exception if any are found.
        if (!unusedScenariosFilter.getScenarios().isEmpty()) {
            throw new CascadeException(format(new StringBuilder()
                    .append("Invalid configuration: Scenario %s not found in any journey: ")
                    .append("This journey generator calculates journeys by finding terminators ")
                    .append("and walking backwards to the steps that start journeys. ")
                    .append("If a step is not found in journeys, it is either dependent on ")
                    .append("steps that don't lead to a journey start, or there are no terminators ")
                    .append("downstream of this step.")
                    .toString(), unusedScenariosFilter.getScenarios().get(0).toString()));
        }

        //go through journeys and remove any that are redundant
        Iterator<Journey> journeyIterator = journeys.iterator();
        while (journeyIterator.hasNext()) {
            Journey journey = journeyIterator.next();

            JourneyImage journeyImage = new JourneyImage();

            for (Journey reference : journeys) {
                if (!reference.equals(journey)) {
                    journeyImage.add(reference.getSteps());
                }
            }

            if (journeyImage.match(journey.getSteps())) {
                journeyIterator.remove();
            }
        }

        int index = 1;
        for (Journey journey : journeys) {
            journey.init(index);
            index++;
        }

        sort(journeys, new Comparator<Journey>() {
            @Override
            public int compare(Journey lhs, Journey rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        return journeys;
    }

    private void generatingTrail(Class currentScenario, List<Class> trail, List<Class> allScenarios, List<Journey> journeys, Class<?> controlClass, Filter filter) {

        //walk backwards up the trail looking for a repeat of the currentScenario. If one appears before a ReEntrantTerminator, then we have an infinite cycle.
        ListIterator<Class> iterator = trail.listIterator(trail.size());
        while (iterator.hasPrevious()) {
            Class scenario = iterator.previous();

            //A ReEntrantTerminator has a limit to its entry in a journey.  So there cannot be infinite cycles if we have encountered this here.
            if (findAnnotation(ReEntrantTerminator.class, scenario) != null) {
                break;
            }

            //Normal terminators are ok too.
            if (findAnnotation(Terminator.class, scenario) != null) {
                break;
            }


            //if we find the current scenario in the trail already, without ReEntrantTerminators between, we have an infinite loop.
            if (scenario == currentScenario) {
                List<Class> infiniteLoop = new ArrayList<Class>();

                while (iterator.hasNext()) {
                    infiniteLoop.add(iterator.next());
                }
                infiniteLoop.add(currentScenario);

                Collections.reverse(infiniteLoop);

                StringBuilder buffer = new StringBuilder();
                for (Class cls : infiniteLoop) {
                    String[] parts = cls.toString().split("[.]");
                    buffer.append(parts[parts.length - 1]);
                    buffer.append(" ");
                }

                throw new CascadeException(format("Invalid configuration.  An infinite loop is configured. [%s]", buffer.toString().trim()));
            }
        }

        trail.add(currentScenario);

        Step currentStepAnnotation = findAnnotation(Step.class, currentScenario);

        boolean beginningOfTrail = currentStepAnnotation.value()[0] == Step.Null.class;

        if (beginningOfTrail) {
            //we are copying the trail as the current trail may be part of another trail.  There could be a few of them.
            List<Class> newTrail = new ArrayList<Class>(trail);
            Collections.reverse(newTrail);

            Journey candidateJourney = new Journey(newTrail, controlClass);
            if (filter.match(candidateJourney)) {
                journeys.add(candidateJourney);
            }
        } else {

            for (Class preceedingStep : currentStepAnnotation.value()) {

                Scenario:
                for (Class scenario : allScenarios) {

                    //if this scenario doesn't preceed the current step, we don't use it.
                    boolean scenarioIsNotAPreceedingStep = !preceedingStep.isAssignableFrom(scenario);
                    if (scenarioIsNotAPreceedingStep) {
                        continue;
                    }

                    //if this scenario is a terminator we don't use it.
                    boolean isATerminatingScenario = findAnnotation(Terminator.class, scenario) != null;
                    if (isATerminatingScenario) {
                        continue;
                    }

                    //if this scenario is a ReEntrantTerminator and its limit is reached, we don't use it.
                    ReEntrantTerminator reEntrantTerminator = findAnnotation(ReEntrantTerminator.class, scenario);
                    if (reEntrantTerminator != null) {
                        Integer limit = reEntrantTerminator.value();
                        Integer count = 0;
                        for (Class cls : trail) {
                            if (cls == scenario) {
                                count++;
                            }
                            if (count == limit) {
                                continue Scenario;
                            }
                        }
                    }

                    //if the journey already has a scenario for this scenario's step, and it is different from this scenario, we don't use it.
                    for (Class cls : trail) {

                        if (preceedingStep.isAssignableFrom(cls) && cls != scenario) {
                            continue Scenario;
                        }
                    }
                    generatingTrail(scenario, trail, allScenarios, journeys, controlClass, filter);
                }
            }
        }
        trail.remove(trail.size() - 1);
    }

    private void findTerminatngScenarios(List<Class> allScenarios, List<Class> terminators) {
        for (Class<?> scenario : allScenarios) {
            boolean isATerminatingScenario = findAnnotation(Terminator.class, scenario) != null;

            if (isATerminatingScenario) {
                terminators.add(scenario);
            }
        }
    }

    private void findReEntrantTerminatngScenarios(List<Class> allScenarios, List<Class> terminators) {
        for (Class<?> scenario : allScenarios) {
            boolean isATerminatingScenario = findAnnotation(ReEntrantTerminator.class, scenario) != null;

            if (isATerminatingScenario) {
                terminators.add(scenario);
            }
        }
    }

    private void findDanglingScenarios(List<Class> allScenarios, List<Class> terminators) {

        //collect all scenarios that don't have @RunWith
        List<Class> friendlyScenarios = new ArrayList<Class>();
        for (Class scenario : allScenarios) {
            if (getValueOfFieldAnnotatedWith(newInstance(scenario, "step"), OnlyRunWith.class) == null) {
                friendlyScenarios.add(scenario);
            }
        }


        List<Class> possibleTerminators = new ArrayList<Class>(allScenarios);
        for (Iterator<Class> i = possibleTerminators.iterator(); i.hasNext(); ) {
            Class clz = i.next();
            boolean isATerminatingScenario = findAnnotation(Terminator.class, clz) != null;
            if (isATerminatingScenario) {
                i.remove();
            }
        }


        List<Class> implicitTerminators = new ArrayList<Class>(possibleTerminators);
        for (Class possibleTerminator : possibleTerminators) {
            for (Class scenario : friendlyScenarios) { //if a friendly scenario references the possible terminator, it is not a terminator
                for (Class<?> referencedStep : findAnnotation(Step.class, scenario).value()) {
                    if (referencedStep.isAssignableFrom(possibleTerminator)) {
                        implicitTerminators.remove(possibleTerminator);
                    }
                }
            }
        }

        //At this point the scenarios that are remaining in the implicitTerminators list are:
        //1. scenarios that are not referenced by anything
        //2. scenarios that are only referenced by @RunWith scenarios (we will only know if they are really there once we consider the journey)
        terminators.addAll(implicitTerminators);

    }


    private <T extends Annotation> T findAnnotation(Class<T> annotationClass, Class<?> subject) {
        T step = subject.getAnnotation(annotationClass);
        if (step != null) {
            return step;
        }

        for (Class<?> i : subject.getInterfaces()) {
            step = i.getAnnotation(annotationClass);
            if (step != null) {
                return step;
            }
        }

        Class superClass = subject.getSuperclass();
        if (superClass != null) {
            return findAnnotation(annotationClass, superClass);
        }
        return null;
    }


    private static class CompositeFilter implements Filter {

        private Filter[] filters;

        public CompositeFilter(Filter... filters) {
            this.filters = filters;
        }

        @Override
        public boolean match(Journey journey) {
            for (Filter filter : filters) {
                if (!filter.match(journey)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class OnlyRunWithFilter implements Filter {

        private ConditionalLogic conditionalLogic;

        public OnlyRunWithFilter(ConditionalLogic conditionalLogic) {
            this.conditionalLogic = conditionalLogic;
        }

        @Override
        public boolean match(Journey journey) {

            Iterator<Class> stepIterator = journey.getSteps().iterator();
            while (stepIterator.hasNext()) {
                Class step = stepIterator.next();
                Predicate predicate = (Predicate) getValueOfFieldAnnotatedWith(newInstance(step, "step"), OnlyRunWith.class);
                if (predicate != null) {
                    boolean valid = conditionalLogic.matches(predicate, journey.getSteps());
                    if (!valid) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static class UnusedScenariosFilter implements Filter {

        private List<Class> scenarios;

        public UnusedScenariosFilter(List<Class> scenarios) {
            this.scenarios = new ArrayList<Class>(scenarios);
        }

        @Override
        public boolean match(Journey journey) {

            for (Class step : journey.getSteps()) {
                scenarios.remove(step);
            }

            return true;
        }

        public List<Class> getScenarios() {
            return scenarios;
        }
    }

    private static class RedundantFilter implements Filter {

        private JourneyImage journeyImage = new JourneyImage();

        @Override
        public boolean match(Journey journey) {

            if (journeyImage.match(journey.getSteps())) {
                return false;
            } else {
                journeyImage.add(journey.getSteps());
                return true;
            }
        }
    }

    private static class JourneyImage {

        private List<List<Class>> image = new ArrayList<List<Class>>();

        public boolean match(List<Class> steps) {

            if (image.size() < steps.size()) {
                return false;
            }

            for (int i = 0; i < steps.size(); i++) {
                if (!image.get(i).contains(steps.get(i))) {
                    return false;
                }
            }
            return true;
        }

        public void add(List<Class> steps) {
            for (int i = 0; i < steps.size(); i++) {
                if (image.size() == i) {
                    image.add(new ArrayList<Class>());
                }
                if (!image.get(i).contains(steps.get(i))) {
                    image.get(i).add(steps.get(i));
                }
            }
        }
    }

    private class ClassComparator implements Comparator<Class> {
        @Override
        public int compare(Class lhs, Class rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }


}
