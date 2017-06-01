package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.util.Map;

public class PathFunction implements Function<Object> {

    private String path;

    public PathFunction(String path) {
        this.path = path;
    }

    @Override
    public Object apply(Map<String, Object> scope) {

        String[] tokens = path.split("[.]]");
        Map<String, Object> current = scope;
        for (int i = 0; i < tokens.length - 1; i++) {
            Object map = current.get(tokens[i]);
            if (map instanceof Map) {
                current = (Map<String, Object>) map;
            } else {
                throw new RuntimeException("Path cannot walk over primitive");
            }
        }

        return current.get(tokens[tokens.length - 1]);
    }
}
