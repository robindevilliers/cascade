package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.util.List;
import java.util.Map;

public class LowerCaseFunction implements Function<String> {

    private Function<?> argument;

    public LowerCaseFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression. LowerCase only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public String apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof String)) {
            throw new RuntimeException("Invalid expression. LowerCase takes a string as its argument.");
        }
        String string = (String) value;
        return string.toLowerCase();
    }
}
