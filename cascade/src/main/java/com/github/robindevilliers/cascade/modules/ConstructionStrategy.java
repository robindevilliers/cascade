package com.github.robindevilliers.cascade.modules;

import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.utils.Reference;
import com.github.robindevilliers.cascade.model.Journey;

import java.util.List;
import java.util.Map;

public interface ConstructionStrategy {

    Map<String, Scope> setup(Class<?> controlClass, Journey journey, Reference<Object> control, Reference<List<Object>> steps, Map<String, Scope> globalScope);

    void tearDown(Reference<Object> control, Journey journey, Reference<List<Object>> steps);
}
