package uk.co.malbec.cascade.modules;


import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.generator.Filter;

public interface FilterStrategy extends Filter {
    void init(Class<?> controlClass);
    boolean match(Journey journey);
}
