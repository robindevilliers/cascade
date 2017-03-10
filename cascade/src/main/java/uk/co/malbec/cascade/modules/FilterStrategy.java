package uk.co.malbec.cascade.modules;


import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.generator.Filter;

import java.util.Map;

public interface FilterStrategy extends Filter {
    void init(Class<?> controlClass, Map<String, Scope> globalScope);
    boolean match(Journey journey);
}
