package com.github.robindevilliers.cascade.modules.reporter;

public interface StateRenderingStrategy {
    boolean accept(Object value);
    String render(Object value);
}
