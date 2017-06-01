package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.util.Map;

public class NotFunction implements Function<Boolean> {

    private Function<Boolean> function;

    public NotFunction(Function<Boolean> function){
        this.function = function;
    }

    @Override
    public Boolean apply(Map<String, Object> scope) {
        return !function.apply(scope);
    }
}
