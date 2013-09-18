package uk.co.malbec.cascade.utils;


public class Reference<T> {
    
    private T object;

    public T get() {
        return object;
    }

    public void set(T object) {
        this.object = object;
    }
}
