package uk.co.malbec.cascade.modules;

import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.utils.Reference;

import java.util.List;
import java.util.Map;

public interface ConstructionStrategy {
    Map<String, Scope> setup(Class<?> controlClass, Journey journey, Reference<Object> control, Reference<List<Object>> steps);
    void tearDown( Reference<Object> control, Reference<List<Object>> steps);
}
