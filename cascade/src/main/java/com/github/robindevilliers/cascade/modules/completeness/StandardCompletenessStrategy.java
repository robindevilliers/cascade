package com.github.robindevilliers.cascade.modules.completeness;

import com.github.robindevilliers.cascade.Completeness;
import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.annotations.CompletenessLevel;
import com.github.robindevilliers.cascade.annotations.When;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.modules.CompletenessStrategy;
import com.github.robindevilliers.cascade.utils.ReflectionUtils;

import java.util.*;

public class StandardCompletenessStrategy implements CompletenessStrategy {

    private Completeness completeness = Completeness.UNRESTRICTED;

    @Override
    public void init(Class<?> controlClass, Map<String, Scope> globalScope) {
        CompletenessLevel completenessLevel = controlClass.getAnnotation(CompletenessLevel.class);
        if (completenessLevel != null) {
            completeness = completenessLevel.value();
        }
    }

    @Override
    public List<Journey> filter(List<Journey> journeys) {

        if (completeness == Completeness.UNRESTRICTED) {
            return journeys;
        }

        if (completeness == Completeness.SCENARIO_COMPLETE) {
            return new ScenarioDispersalSelectionAlgorithm() {
                @Override
                public String supplyGroupingId(Scenario scenario) {
                    return scenario.getClazz().getCanonicalName();
                }
            }.calculate(journeys);
        }

        if (completeness == Completeness.TRANSITION_COMPLETE) {
            return new ScenarioDispersalSelectionAlgorithm() {
                @Override
                public String supplyGroupingId(Scenario scenario) {
                    When when = ReflectionUtils.findMethodAnnotation(When.class, scenario.getClazz());
                    if (when == null) {
                        return null;
                    }
                    return scenario.getClazz().getCanonicalName();
                }
            }.calculate(journeys);
        }

        if (completeness == Completeness.STATE_COMPLETE) {
            return new ScenarioDispersalSelectionAlgorithm() {
                @Override
                public String supplyGroupingId(Scenario scenario) {
                    return scenario.getStateClazz().getCanonicalName();
                }
            }.calculate(journeys);

        }

        throw new UnsupportedOperationException("Unknown completeness level supplied.");
    }

    @Override
    public Completeness getCompletenessLevel() {
        return this.completeness;
    }


    private static abstract class ScenarioDispersalSelectionAlgorithm {

        public abstract String supplyGroupingId(Scenario scenario);

        List<Journey> calculate(List<Journey> journeys) {
            //build up a histogram of scenarios
            Map<String, ScenarioWrapper> histogram = new HashMap<>();

            //group all journeys by scenarioId, transitionId or stateId.
            for (Journey journey : journeys) {
                for (Scenario scenario : journey.getSteps()) {
                    String groupingId = supplyGroupingId(scenario);
                    if (groupingId != null){
                        ScenarioWrapper scenarioWrapper = histogram
                                .computeIfAbsent(groupingId, k -> new ScenarioWrapper(scenario));
                        scenarioWrapper.add(journey);
                    }
                }
            }

            //assign to each scenario an Order, the higher the order, the rarer the scenario.
            Set<Integer> sizes = new HashSet<>();
            for (ScenarioWrapper scenarioWrapper : histogram.values()) {
                sizes.add(scenarioWrapper.getNumberOfJourneys());
            }

            int c = 0;
            for (Integer size : sizes) {
                for (ScenarioWrapper scenarioWrapper : histogram.values()) {
                    if (scenarioWrapper.getNumberOfJourneys() == size) {
                        int order = sizes.size() - c;
                        scenarioWrapper.setOrder(order);
                    }
                }
                c++;
            }

            List<Journey> results = new ArrayList<>();
            while (true) {
                //calculate the value of each journey and find the most valuable.
                Journey mostValuable = null;
                int value = 0;
                for (Journey journey : journeys) {
                    int v = 0;
                    for (ScenarioWrapper scenarioWrapper : histogram.values()) {
                        if (scenarioWrapper.containsJourney(journey)) {
                            v += scenarioWrapper.getOrder();
                        }
                    }
                    if (v > value) {
                        mostValuable = journey;
                        value = v;
                    }
                }

                //if we have a journey worth more than zero, add it to our results.
                if (mostValuable != null) {
                    results.add(mostValuable);
                    //now go and mark all scenarios this journey references as zero Order.
                    for (Scenario scenario : mostValuable.getSteps()) {
                        for (ScenarioWrapper scenarioWrapper : histogram.values()) {
                            if (scenarioWrapper.getScenario().equals(scenario)) {
                                scenarioWrapper.setOrder(0);
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            return results;
        }

    }

    private static class ScenarioWrapper {

        private Scenario scenario;

        private Set<Journey> journeys = new HashSet<>();

        private int order;

        ScenarioWrapper(Scenario scenario) {
            this.scenario = scenario;
        }

        public void add(Journey journey) {
            this.journeys.add(journey);
        }

        int getNumberOfJourneys() {
            return journeys.size();
        }

        public void setOrder(int order) {
            this.order = order;
        }

        boolean containsJourney(Journey journey) {
            return journeys.contains(journey);
        }

        public Scenario getScenario() {
            return scenario;
        }

        public int getOrder() {
            return order;
        }
    }
}
