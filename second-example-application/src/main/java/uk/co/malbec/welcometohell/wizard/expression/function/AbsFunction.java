package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.List;
import java.util.Map;

public class AbsFunction implements Function<Integer> {

    private Function<?> argument;

    public AbsFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression. Abs only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public Integer apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof Integer)) {
            throw new RuntimeException("Invalid expression. Abs takes a Integer as its argument.");
        }
        return Math.abs((Integer) value);
    }
}
