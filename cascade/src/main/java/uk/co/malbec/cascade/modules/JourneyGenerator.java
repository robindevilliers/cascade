package uk.co.malbec.cascade.modules;


import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.generator.Filter;

import java.util.List;
import java.util.Map;

public interface JourneyGenerator {
    List<Journey> generateJourneys(List<Scenario> scenarios, Class<?> controlClass, Filter filter, Map<String, Scope> globalScope);
}
