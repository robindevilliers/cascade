package uk.co.malbec.cascade;


import org.junit.runner.*;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.annotations.Description;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.lang.String.format;
import static uk.co.malbec.cascade.utils.ReflectionUtils.getValueOfFieldAnnotatedWith;
import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class StepBackwardsFromTerminatorsJourneyGenerator implements JourneyGenerator {

    private ConditionalLogic conditionalLogic;

    public StepBackwardsFromTerminatorsJourneyGenerator(ConditionalLogic conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    public List<Journey> generateJourneys(List<Class> allScenarios, Class<?> controlClass) {

        List<Class> terminators = new ArrayList<Class>();

        //these are scenarios marked with a Terminator annotation - explicit terminators.
        findTerminatngScenarios(allScenarios, terminators);

        //these are scenarios marked with a ReEntrantTerminator annotation - these are terminators but only after already being in the journey n number of times.
        findReEntrantTerminatngScenarios(allScenarios, terminators);

        //these are scenarios that belong to steps that are not followed by other steps - implicit terminators.
        findDanglingScenarios(allScenarios, terminators);

        List<Journey> journeys = new ArrayList<Journey>();
        for (Class terminator : terminators) {
            generatingTrail(terminator, new ArrayList<Class>(), allScenarios, journeys, controlClass);
        }

        // go through journeys and make sure that they are valid according to @RunWith annotations.
        Iterator<Journey> journeyIterator = journeys.iterator();
        while (journeyIterator.hasNext()) {
            Journey journey = journeyIterator.next();
            Iterator<Class> stepIterator = journey.getSteps().iterator();
            while (stepIterator.hasNext()) {
                Class step = stepIterator.next();

                Predicate predicate = (Predicate) getValueOfFieldAnnotatedWith(newInstance(step, "step"), OnlyRunWith.class);

                if (predicate != null) {
                    boolean valid = conditionalLogic.matches(predicate, journey.getSteps());
                    if (!valid) {
                        //test if the step that has the annotation is the last one in the list.
                        //if it is, then the preceeding step is a candidate for an implicit terminator
                        if (!stepIterator.hasNext()) {
                            stepIterator.remove();
                        } else {
                            journeyIterator.remove();
                        }
                    }
                }
            }
        }

        //Go through journeys and remove journeys that are subsets of other journeys.
        Collections.sort(journeys, new Comparator<Journey>() {
            @Override
            public int compare(Journey lhs, Journey rhs) {
                return lhs.getSteps().size() - rhs.getSteps().size();
            }
        });


        Iterator<Journey> lhsIterator = journeys.listIterator();
        int end = journeys.size();
        for (int start = 1; lhsIterator.hasNext(); start++) {
            Journey lhsJourney = lhsIterator.next();

            for (Journey rhsJourney : journeys.subList(start, end)) {
                if (lhsJourney.getSteps().equals(rhsJourney.getSteps().subList(0, lhsJourney.getSteps().size()))) {
                    lhsIterator.remove();
                    start--;
                    end = journeys.size();
                    break;
                }
            }
        }

        for (Class scenario : allScenarios) {
            boolean found = false;
            for (Journey journey : journeys) {
                if (journey.getSteps().contains(scenario)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new CascadeException(format(new StringBuilder()
                        .append("Invalid configuration: Scenario %s not found in any journey: ")
                        .append("This journey generator calculates journeys by finding terminators ")
                        .append("and walking backwards to the steps that start journeys. ")
                        .append("If a step is not found in journeys, it is either dependent on ")
                        .append("steps that don't lead to a journey start, or there are no terminators ")
                        .append("downstream of this step.")
                        .toString(), scenario.toString()));
            }
        }

        for (Journey journey : journeys) {
            journey.init();
        }

        Collections.sort(journeys, new Comparator<Journey>() {
            @Override
            public int compare(Journey lhs, Journey rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        return journeys;
    }

    private void generatingTrail(Class currentScenario, List<Class> trail, List<Class> allScenarios, List<Journey> journeys, Class<?> controlClass) {

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
            journeys.add(new Journey(newTrail, controlClass));

        } else {

            for (Class preceedingStep : currentStepAnnotation.value()) {

                Scenario:
                for (Class scenario : allScenarios) {

                    boolean scenarioIsNotAPreceedingStep = !preceedingStep.isAssignableFrom(scenario);
                    if (scenarioIsNotAPreceedingStep) {
                        continue;
                    }

                    boolean isATerminatingScenario = findAnnotation(Terminator.class, scenario) != null;
                    if (isATerminatingScenario) {
                        continue;
                    }

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


                    generatingTrail(scenario, trail, allScenarios, journeys, controlClass);
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
        List<Class> unreferencedScenarios = new ArrayList<Class>(allScenarios);


        for (Iterator<Class> i = unreferencedScenarios.iterator(); i.hasNext(); ) {
            Class clz = i.next();
            boolean isATerminatingScenario = findAnnotation(Terminator.class, clz) != null;
            if (isATerminatingScenario) {
                i.remove();
            }
        }

        for (Class<?> scenario : allScenarios) {
            Step stepAnnotation = findAnnotation(Step.class, scenario);
            for (Class<?> preceedingStep : stepAnnotation.value()) {

                for (Iterator<Class> i = unreferencedScenarios.iterator(); i.hasNext(); ) {
                    Class clz = i.next();
                    //this tests if a scenario implements a step class.
                    boolean isNotATerminator = preceedingStep.isAssignableFrom(clz);

                    if (isNotATerminator) {
                        i.remove();
                    }
                }
            }
        }
        terminators.addAll(unreferencedScenarios);
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

}
