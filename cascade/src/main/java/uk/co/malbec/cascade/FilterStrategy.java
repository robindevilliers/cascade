package uk.co.malbec.cascade;


import uk.co.malbec.cascade.model.Journey;

public interface FilterStrategy {
    void init(Class<?> controlClass);
    boolean match(Journey journey);
}
