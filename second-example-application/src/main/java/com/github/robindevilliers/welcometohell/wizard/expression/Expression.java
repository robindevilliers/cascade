package com.github.robindevilliers.welcometohell.wizard.expression;

import java.util.Map;

public class Expression {

    private Function<Boolean> predicate;

    public Expression(Function<Boolean> predicate){
        this.predicate = predicate;
    }

    public boolean matches(Map<String, Object> data) {
        return predicate.apply(data);
    }
}
