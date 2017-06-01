package com.github.robindevilliers.cascade.modules;


import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.modules.generator.Filter;

import java.util.List;
import java.util.Map;

public interface JourneyGenerator {

    void init(ConditionalLogic conditionalLogic);

    List<Journey> generateJourneys(List<Scenario> scenarios, Class<?> controlClass, Filter filter, Map<String, Scope> globalScope);
}
