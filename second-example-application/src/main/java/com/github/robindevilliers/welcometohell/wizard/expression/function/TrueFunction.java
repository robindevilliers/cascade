package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.util.Map;

public class TrueFunction implements Function<Boolean> {
    @Override
    public Boolean apply(Map<String, Object> scope) {
        return true;
    }
}
