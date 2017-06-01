package com.github.robindevilliers.cascade;

import com.github.robindevilliers.cascade.modules.reporter.StateRenderingStrategy;
import com.github.robindevilliers.cascade.modules.reporter.TransitionRenderingStrategy;

public class Scope {

    private boolean global = false;
    private Object value;
    private StateRenderingStrategy stateRenderingStrategy;
    private TransitionRenderingStrategy transitionRenderingStrategy;
    private Object copy;

    public Scope(Object value, StateRenderingStrategy stateRenderingStrategy, TransitionRenderingStrategy transitionRenderingStrategy){
        this.value = value;
        this.stateRenderingStrategy = stateRenderingStrategy;
        this.transitionRenderingStrategy = transitionRenderingStrategy;
    }

    public Scope(Object value){
        this.value = value;
    }

    public Scope setGlobal(){
        this.global = true;
        return this;
    }

    public boolean isGlobal() {
        return global;
    }

    public Object getValue() {
        return value;
    }

    public StateRenderingStrategy getStateRenderingStrategy() {
        return stateRenderingStrategy;
    }

    public TransitionRenderingStrategy getTransitionRenderingStrategy() {
        return transitionRenderingStrategy;
    }

    public Object getCopy() {
        return copy;
    }

    public void setCopy(Object copy) {
        this.copy = copy;
    }
}
