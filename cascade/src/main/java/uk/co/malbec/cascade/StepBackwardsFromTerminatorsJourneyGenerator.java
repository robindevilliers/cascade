package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StepBackwardsFromTerminatorsJourneyGenerator implements JourneyGenerator {

    public List<Journey> generateJourneys(List<Class> allScenarios) {

        List<Class> terminators = new ArrayList<Class>();

        //these are scenarios marked with a Terminator annotation - explicit terminators.
        findTerminatngScenarios(allScenarios, terminators);

        //these are scenarios that belong to steps that are not followed by other steps - implicit terminators.
        findDanglingScenarios(allScenarios, terminators);

        List<Journey> journeys = new ArrayList<Journey>();
        for (Class terminator : terminators) {
            generatingTrail(terminator, new ArrayList<Class>(), allScenarios, journeys);
        }
        return journeys;
    }

    private void generatingTrail(Class currentScenario, List<Class> trail, List<Class> allScenarios, List<Journey> journeys) {
        trail.add(currentScenario);

        Step currentStepAnnotation = findAnnotation(Step.class, currentScenario);

        boolean beginningOfTrail = currentStepAnnotation.value()[0] == Step.Null.class;

        if (beginningOfTrail) {

            List<Class> newTrail = new ArrayList<Class>(trail);
            Collections.reverse(newTrail);
            journeys.add(new Journey(newTrail));

        } else {

            for (Class preceedingStep : currentStepAnnotation.value()) {


                for (Class scenario : allScenarios) {

                    boolean scenarioIsNotAPreceedingStep = !preceedingStep.isAssignableFrom(scenario);
                    if (scenarioIsNotAPreceedingStep) {
                        continue;
                    }

                    boolean isATerminatingScenario = findAnnotation(Terminator.class, scenario) != null;
                    if (isATerminatingScenario) {
                        continue;
                    }

                    generatingTrail(scenario, trail, allScenarios, journeys);

                }

            }

        }
        trail.remove(trail.size() - 1);
    }
    
    private void findTerminatngScenarios(List<Class> allScenarios, List<Class> terminators){
        for (Class<?> scenario : allScenarios) {
            boolean isATerminatingScenario = findAnnotation(Terminator.class, scenario) != null;

            if (isATerminatingScenario) {
                terminators.add(scenario);
            }
        }
    }
    
    public void findDanglingScenarios(List<Class> allScenarios, List<Class> terminators){
        List<Class> danglingScenarios = new ArrayList<Class>(allScenarios);

        for (Class<?> scenario : allScenarios) {
            Step stepAnnotation = findAnnotation(Step.class, scenario);
            for (Class<?> preceedingStep : stepAnnotation.value()) {

                Iterator<Class> danglingScenariosIterator = danglingScenarios.iterator();

                while (danglingScenariosIterator.hasNext()) {
                    Class potentialTerminator = danglingScenariosIterator.next();

                    boolean isNotATerminator = preceedingStep.isAssignableFrom(potentialTerminator);

                    if (isNotATerminator) {
                        danglingScenariosIterator.remove();
                    }
                }
            }
        }
        terminators.addAll(danglingScenarios);
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
