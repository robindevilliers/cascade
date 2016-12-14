package uk.co.malbec.cascade.modules.generator;


import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.annotations.OnlyRunWith;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.JourneyGenerator;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.sort;
import static java.util.Collections.synchronizedList;
import static uk.co.malbec.cascade.utils.ReflectionUtils.getValueOfFieldAnnotatedWith;
import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class StepBackwardsFromTerminatorsJourneyGenerator implements JourneyGenerator {

    private ConditionalLogic conditionalLogic;

    public StepBackwardsFromTerminatorsJourneyGenerator(ConditionalLogic conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    public List<Journey> generateJourneys(final List<Scenario> allScenarios, final Class<?> controlClass, Filter filter) {

        //sort the scenarios so that the generation of journeys is always deterministic from the users point of view.
        sort(allScenarios, new ClassComparator());

        OnlyRunWithFilter onlyRunWithFilter = new OnlyRunWithFilter(conditionalLogic);
        UnusedScenariosFilter unusedScenariosFilter = new UnusedScenariosFilter(allScenarios);
        RedundantFilter redundantFilter = new RedundantFilter();
        final Filter compositeFilter = new CompositeFilter(onlyRunWithFilter, unusedScenariosFilter, filter, redundantFilter);

        List<Scenario> terminators = new ArrayList<>();

        //these are scenarios marked with a Terminator annotation - explicit terminators.
        findTerminatngScenarios(allScenarios, terminators);

        //these are scenarios marked with a ReEntrantTerminator annotation - these are terminators but only after already being in the journey n number of times.
        findReEntrantTerminatngScenarios(allScenarios, terminators);

        //these are scenarios that belong to steps that are not followed by other steps - implicit terminators.
        findDanglingScenarios(allScenarios, terminators);

        //sort terminators so that we always generate the same journeys.  This guarantees that we always have the same number of tests in the first pass.
        sort(terminators, new ClassComparator());

        final List<Journey> journeys = new ArrayList<>();
        for (final Scenario terminator : terminators) {
            generatingTrail(terminator, new ArrayList<Scenario>(), allScenarios, journeys, controlClass, compositeFilter);
        }

        //go through all scenarios and find scenarios that are not in any journeys and generate an exception if any are found.
        if (!unusedScenariosFilter.getScenarios().isEmpty()) {
            throw new CascadeException(format("Invalid configuration: Scenario %s not found in any journey: " +
                    "This journey generator calculates journeys by finding terminators " +
                    "and walking backwards to the steps that start journeys. " +
                    "If a step is not found in journeys, it is either dependent on " +
                    "steps that don't lead to a journey start, or there are no terminators " +
                    "downstream of this step.", unusedScenariosFilter.getScenarios().get(0).getCls().toString()));
        }

        //go through journeys and remove any that are redundant
        //we generate an image of all other journeys and then test if the current journey is redundant.
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

    private void generatingTrail(Scenario currentScenario, List<Scenario> trail, List<Scenario> allScenarios, List<Journey> journeys, Class<?> controlClass, Filter filter) {

        //walk backwards up the trail looking for a repeat of the currentScenario. If one appears before a ReEntrantTerminator, then we have an infinite cycle.
        ListIterator<Scenario> iterator = trail.listIterator(trail.size());
        while (iterator.hasPrevious()) {
            Scenario scenario = iterator.previous();

            //A ReEntrantTerminator has a limit to its entry in a journey.  So there cannot be infinite cycles if we have encountered this here.
            if (scenario.isReEntrantTerminator()) {
                break;
            }

            //Normal terminators are ok too.
            if (scenario.isTerminator()) {
                break;
            }


            //if we find the current scenario in the trail already, without ReEntrantTerminators between, we have an infinite loop.
            if (scenario == currentScenario) {
                List<Scenario> infiniteLoop = new ArrayList<>();

                while (iterator.hasNext()) {
                    infiniteLoop.add(iterator.next());
                }
                infiniteLoop.add(currentScenario);

                Collections.reverse(infiniteLoop);

                StringBuilder buffer = new StringBuilder();
                for (Scenario s : infiniteLoop) {
                    String[] parts = s.getCls().toString().split("[.]");
                    buffer.append(parts[parts.length - 1]);
                    buffer.append(" ");
                }

                throw new CascadeException(format("Invalid configuration.  An infinite loop is configured. [%s]", buffer.toString().trim()));
            }
        }

        trail.add(currentScenario);

        boolean beginningOfTrail = currentScenario.getSteps()[0] == Step.Null.class;

        if (beginningOfTrail) {
            //we are copying the trail as the current trail may be part of another trail.  There could be a few of them.
            List<Scenario> newTrail = new ArrayList<>(trail);
            Collections.reverse(newTrail);

            Journey candidateJourney = new Journey(newTrail, controlClass);
            if (filter.match(candidateJourney)) {
                journeys.add(candidateJourney);
            }
        } else {

            for (Class preceedingStep : currentScenario.getSteps()) {

                Scenario:
                for (Scenario scenario : allScenarios) {

                    //if this scenario doesn't preceed the current step, we don't use it.
                    boolean scenarioIsNotAPreceedingStep = !preceedingStep.isAssignableFrom(scenario.getCls());
                    if (scenarioIsNotAPreceedingStep) {
                        continue;
                    }

                    if (scenario.isTerminator()) {
                        continue;
                    }

                    //if this scenario is a ReEntrantTerminator and its limit is reached, we don't use it.

                    if (scenario.isReEntrantTerminator()) {
                        Integer count = 0;
                        for (Scenario s : trail) {
                            if (s == scenario) {
                                count++;
                            }
                            if (count == scenario.getReEntrantCount()) {
                                continue Scenario;
                            }
                        }
                    }

                    //if the journey already has a scenario for this scenario's step, and it is different from this scenario, we don't use it.
                    for (Scenario s : trail) {

                        if (preceedingStep.isAssignableFrom(s.getCls()) && s != scenario) {
                            continue Scenario;
                        }
                    }
                    generatingTrail(scenario, trail, allScenarios, journeys, controlClass, filter);
                }
            }
        }
        trail.remove(trail.size() - 1);
    }

    private void findTerminatngScenarios(List<Scenario> allScenarios, List<Scenario> terminators) {
        for (Scenario scenario : allScenarios) {
            if (scenario.isTerminator()) {
                terminators.add(scenario);
            }
        }
    }

    private void findReEntrantTerminatngScenarios(List<Scenario> allScenarios, List<Scenario> terminators) {
        for (Scenario scenario : allScenarios) {
            if (scenario.isReEntrantTerminator()) {
                terminators.add(scenario);
            }
        }
    }

    private void findDanglingScenarios(List<Scenario> allScenarios, List<Scenario> terminators) {

        //collect all scenarios that don't have @RunWith
        List<Scenario> friendlyScenarios = new ArrayList<>();
        for (Scenario scenario : allScenarios) {
            if (getValueOfFieldAnnotatedWith(newInstance(scenario.getCls(), "step"), OnlyRunWith.class) == null) {
                friendlyScenarios.add(scenario);
            }
        }


        List<Scenario> possibleTerminators = new ArrayList<>(allScenarios);
        for (Iterator<Scenario> i = possibleTerminators.iterator(); i.hasNext(); ) {
            Scenario scenario = i.next();
            if (scenario.isTerminator()) {
                i.remove();
            }
        }

        //go through the friendly scenarios (scenarios we know will be in a journey, no @RunWith), and remove scenarios that are referenced by them.
        List<Scenario> implicitTerminators = new ArrayList<>(possibleTerminators);
        for (Scenario possibleTerminator : possibleTerminators) {
            for (Scenario scenario : friendlyScenarios) { //if a friendly scenario references the possible terminator, it is not a terminator
                for (Class<?> referencedStep : scenario.getSteps()) {
                    if (referencedStep.isAssignableFrom(possibleTerminator.getCls())) {
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
            return (T) findAnnotation(annotationClass, superClass);
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

            for (Scenario step : journey.getSteps()) {
                Predicate predicate = (Predicate) getValueOfFieldAnnotatedWith(newInstance(step.getCls(), "step"), OnlyRunWith.class);
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

        private List<Scenario> scenarios;

        public UnusedScenariosFilter(List<Scenario> scenarios) {
            this.scenarios = synchronizedList(new ArrayList<>(scenarios));
        }

        @Override
        public boolean match(Journey journey) {

            for (Scenario step : journey.getSteps()) {
                scenarios.remove(step);
            }

            return true;
        }

        public List<Scenario> getScenarios() {
            return scenarios;
        }
    }

    private static class RedundantFilter implements Filter {

        private JourneyImage journeyImage = new JourneyImage();

        @Override
        public synchronized boolean match(Journey journey) {

            if (journeyImage.match(journey.getSteps())) {
                return false;
            } else {
                journeyImage.add(journey.getSteps());
                return true;
            }
        }
    }

    private static class JourneyImage {

        private List<List<Scenario>> image = new ArrayList<>();

        public boolean match(List<Scenario> steps) {

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

        public void add(List<Scenario> steps) {
            for (int i = 0; i < steps.size(); i++) {
                if (image.size() == i) {
                    image.add(new ArrayList<Scenario>());
                }
                if (!image.get(i).contains(steps.get(i))) {
                    image.get(i).add(steps.get(i));
                }
            }
        }
    }

    private class ClassComparator implements Comparator<Scenario> {
        @Override
        public int compare(Scenario lhs, Scenario rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }


}
