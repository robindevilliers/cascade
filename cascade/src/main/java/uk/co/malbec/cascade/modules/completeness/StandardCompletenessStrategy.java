package uk.co.malbec.cascade.modules.completeness;

import uk.co.malbec.cascade.Completeness;
import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.annotations.CompletenessLevel;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.CompletenessStrategy;

import java.util.*;

import static uk.co.malbec.cascade.utils.ReflectionUtils.findMethodAnnotation;

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
                    Then then = findMethodAnnotation(Then.class, scenario.getCls());
                    if (then == null) {
                        return null;
                    }
                    return then.scenarioId() == null || then.scenarioId().equals(Then.Null) ? scenario.getCls().getCanonicalName() : then.scenarioId();
                }
            }.calculate(journeys);
        }

        if (completeness == Completeness.TRANSITION_COMPLETE) {
            return new ScenarioDispersalSelectionAlgorithm() {
                @Override
                public String supplyGroupingId(Scenario scenario) {
                    When when = findMethodAnnotation(When.class, scenario.getCls());
                    if (when == null) {
                        return null;
                    }
                    return when.transitionId() == null || when.transitionId().equals(When.Null) ? scenario.getCls().getCanonicalName() : when.transitionId();
                }
            }.calculate(journeys);
        }

        if (completeness == Completeness.STATE_COMPLETE) {
            return new ScenarioDispersalSelectionAlgorithm() {
                @Override
                public String supplyGroupingId(Scenario scenario) {
                    Then then = findMethodAnnotation(Then.class, scenario.getCls());
                    if (then == null) {
                        return null;
                    }
                    return then.stateId() == null || then.stateId().equals(When.Null) ? scenario.getStateCls().getCanonicalName() : then.stateId();
                }
            }.calculate(journeys);

        }


        throw new UnsupportedOperationException("Unknown completeness level supplied.");
    }


    private static abstract class ScenarioDispersalSelectionAlgorithm {

        public abstract String supplyGroupingId(Scenario scenario);

        public List<Journey> calculate(List<Journey> journeys) {
            //build up a histogram of scenarios
            Map<String, ScenarioWrapper> histogram = new HashMap<>();

            //group all journeys by scenarioId, transitionId or stateId.
            for (Journey journey : journeys) {
                for (Scenario scenario : journey.getSteps()) {
                    String groupingId = supplyGroupingId(scenario);
                    ScenarioWrapper scenarioWrapper = histogram.get(groupingId);
                    if (scenarioWrapper == null) {
                        scenarioWrapper = new ScenarioWrapper(scenario);
                        histogram.put(groupingId, scenarioWrapper);
                    }
                    scenarioWrapper.add(journey);
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

        public ScenarioWrapper(Scenario scenario) {
            this.scenario = scenario;
        }

        public void add(Journey journey) {
            this.journeys.add(journey);
        }

        public int getNumberOfJourneys() {
            return journeys.size();
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public boolean containsJourney(Journey journey) {
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
