package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.List;
import java.util.Map;

public class EndsWithFunction  implements Function<Boolean> {

    private Function<?> leftArgument;
    private Function<?> rightArgument;

    public EndsWithFunction(List<Function<?>> arguments) {
        if (arguments.size() != 2) {
            throw new RuntimeException("Invalid expression. StartsWith takes two argument");
        }
        this.leftArgument = arguments.get(0);
        this.rightArgument = arguments.get(1);
    }

    @Override
    public Boolean apply(Map<String, Object> scope) {
        Object leftValue = leftArgument.apply(scope);
        Object rightValue = rightArgument.apply(scope);
        if (!(leftValue instanceof String)) {
            throw new RuntimeException("Invalid expression. StartsWith takes a string as its arguments.");
        }
        if (!(rightValue instanceof String)) {
            throw new RuntimeException("Invalid expression. StartsWith takes a string as its arguments.");
        }
        String leftString = (String) leftValue;
        String rightString = (String) rightValue;
        return leftString.endsWith(rightString);
    }
}
