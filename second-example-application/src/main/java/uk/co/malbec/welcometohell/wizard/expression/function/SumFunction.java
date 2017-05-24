package uk.co.malbec.welcometohell.wizard.expression.function;

import org.omg.CORBA.OBJECT_NOT_EXIST;
import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.List;
import java.util.Map;

public class SumFunction implements Function<Integer> {

    private List<Function<?>> arguments;

    public SumFunction(List<Function<?>> arguments) {
        this.arguments = arguments;
    }

    @Override
    public Integer apply(Map<String, Object> scope) {
        int i = 0;
        for (Function<?> f : arguments) {
            Object value = f.apply(scope);
            if (!(value instanceof Integer)) {
                throw new RuntimeException("Invalid Expression. All arguments must be integers");
            }
            i = i + (Integer) value;
        }
        return null;
    }
}
