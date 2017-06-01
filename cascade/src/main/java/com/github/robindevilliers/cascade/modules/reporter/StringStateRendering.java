package com.github.robindevilliers.cascade.modules.reporter;

public class StringStateRendering implements StateRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return value instanceof String;
    }

    @Override
    public String render(Object value) {
        return value.toString();
    }
}
