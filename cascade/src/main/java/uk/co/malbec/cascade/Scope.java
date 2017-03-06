package uk.co.malbec.cascade;

import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;
import uk.co.malbec.cascade.modules.reporter.TransitionRenderingStrategy;

public class Scope {

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
