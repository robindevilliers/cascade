package uk.co.malbec.cascade;


public interface FilterStrategy {
    void init(Class<?> controlClass);
    boolean match(Journey journey);
}
