package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class YearFunction implements Function<Integer> {

    private Function<?> argument;

    public YearFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression.  Year only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public Integer apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof LocalDate)) {
            throw new RuntimeException("Invalid expression. Year takes a date as its argument.");
        }
        LocalDate date = (LocalDate) value;
        return date.getYear();
    }
}
