package com.github.robindevilliers.cascade.modules;


import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.modules.generator.Filter;

import java.util.Map;

public interface FilterStrategy extends Filter {

    void init(Class<?> controlClass, Map<String, Scope> globalScope);

    boolean match(Journey journey);
}
