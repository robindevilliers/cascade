package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.List;
import java.util.Map;

public class UpperCaseFunction implements Function<String> {

    private Function<?> argument;

    public UpperCaseFunction(List<Function<?>> arguments) {
        if (arguments.size() != 1) {
            throw new RuntimeException("Invalid expression. UpperCase only takes one argument");
        }
        this.argument = arguments.get(0);
    }

    @Override
    public String apply(Map<String, Object> scope) {
        Object value = argument.apply(scope);
        if (!(value instanceof String)) {
            throw new RuntimeException("Invalid expression. UpperCase takes a string as its argument.");
        }
        String string = (String) value;
        return string.toUpperCase();
    }
}
