package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DayFunction implements Function<Integer> {

    private Function<?> argument;

    public DayFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression. Day only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public Integer apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof LocalDate)) {
            throw new RuntimeException("Invalid expression. Day takes a date as its argument.");
        }
        LocalDate date = (LocalDate) value;
        return date.getDayOfMonth();
    }
}
