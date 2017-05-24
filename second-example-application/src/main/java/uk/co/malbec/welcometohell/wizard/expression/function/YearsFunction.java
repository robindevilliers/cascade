package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.time.Period;
import java.util.List;
import java.util.Map;

public class YearsFunction implements Function<Integer> {

    private Function<?> argument;

    public YearsFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression.  Years only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public Integer apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof Period)) {
            throw new RuntimeException("Invalid expression. Years takes a date as its argument.");
        }
        Period period = (Period) value;
        return Math.abs(period.getYears());
    }
}
