package com.github.robindevilliers.cascade.modules.reporter;

public interface TransitionRenderingStrategy {
    boolean accept(Object value);
    Object copy(Object value);
    String render(Object value, Object copy);
}
