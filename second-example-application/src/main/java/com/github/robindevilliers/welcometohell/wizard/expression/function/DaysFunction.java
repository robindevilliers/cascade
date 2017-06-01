package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.time.Period;
import java.util.List;
import java.util.Map;

public class DaysFunction implements Function<Integer> {

    private Function<?> argument;

    public DaysFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression.  Days only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public Integer apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof Period)) {
            throw new RuntimeException("Invalid expression. Months takes a date as its argument.");
        }
        Period period = (Period) value;
        return Math.abs(period.getDays());
    }
}
