package com.github.robindevilliers.cascade.modules.generator;

import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.annotations.Steps;
import com.github.robindevilliers.cascade.exception.CascadeException;
import com.github.robindevilliers.cascade.model.Journey;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.List;

class MultiplePreStepsGenerator {

    List<Journey> insertPreStepsToTheJourneys(List<Journey> journeys) {
        List<Journey> copyJourneys = Lists.newArrayList(journeys);
        List<Journey> wholeJourneys = Lists.newArrayList();

        for (Journey journey : copyJourneys) {
            List<Scenario> scenarios = journey.getSteps();
            List<Scenario> theWholeJourney = Lists.newArrayList();

            for (Scenario scenario : scenarios) {
                Steps annotation = scenario.getClazz().getAnnotation(Steps.class);
                if (null != annotation) {
                    Class[] preSteps = annotation.value();
                    for (Class preStep : preSteps) {
                        addPreStepsToTheWholeJourney(theWholeJourney,
                            theScenariosFromStartToThePreStep(journeys, preStep));
                    }
                }
            }

            addPreStepsToTheWholeJourney(theWholeJourney, journey.getSteps());
            wholeJourneys.add(new Journey(
                theWholeJourney,
                getControlClass(journey)));
        }

        return wholeJourneys;
    }

    private void addPreStepsToTheWholeJourney(List<Scenario> theWholeJourney,
        List<Scenario> thePreJourney) {
        for (Scenario scenario : thePreJourney) {
            if (!theWholeJourney.contains(scenario)) {
                theWholeJourney.add(scenario);
            }
        }
    }

    private List<Scenario> theScenariosFromStartToThePreStep(List<Journey> journeys,
        Class preStep) {
        for (Journey journey : journeys) {
            List<Scenario> theScenarios = Lists.newArrayList();
            for (Scenario scenario : journey.getSteps()) {
                theScenarios.add(scenario);
                if (scenario.getClazz().equals(preStep)) {
                    return theScenarios;
                }
            }
        }
        return Lists.newArrayList();
    }

    private Class getControlClass(Journey journey) {
        try {
            Field controlClassField = Journey.class.getDeclaredField("controlClass");
            controlClassField.setAccessible(true);
            return (Class) controlClassField.get(journey);
        } catch (Exception e) {
            throw new CascadeException(e.getMessage());
        }
    }
}
