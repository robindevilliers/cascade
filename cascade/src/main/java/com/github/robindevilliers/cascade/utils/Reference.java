package com.github.robindevilliers.cascade.utils;


public class Reference<T> {
    
    private T object;

    public Reference(){
    }

    public Reference(T object){
        this.object = object;
    }

    public T get() {
        return object;
    }

    public void set(T object) {
        this.object = object;
    }
}
