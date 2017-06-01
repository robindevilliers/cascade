package com.github.robindevilliers.cascade.modules;

import com.github.robindevilliers.cascade.Completeness;
import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.model.Journey;

import java.util.List;
import java.util.Map;

public interface CompletenessStrategy {

    void init(Class<?> controlClass, Map<String, Scope> globalScope);

    List<Journey> filter(List<Journey> journeys);

    Completeness getCompletenessLevel();
}
