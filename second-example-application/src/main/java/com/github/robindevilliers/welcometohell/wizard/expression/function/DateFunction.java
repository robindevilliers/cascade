package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DateFunction implements Function<LocalDate> {

    private Function<?> argument;

    public DateFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression.  Date only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public LocalDate apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof String)) {
            throw new RuntimeException("Invalid expression. Date takes a string as its argument.");
        }
        return LocalDate.parse((String) value);
    }
}
