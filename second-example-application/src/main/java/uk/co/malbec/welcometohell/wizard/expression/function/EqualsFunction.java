package uk.co.malbec.welcometohell.wizard.expression.function;

import uk.co.malbec.welcometohell.wizard.expression.Function;

import java.util.Map;

public class EqualsFunction implements Function<Boolean> {

    private Function<?> lhs;
    private Function<?> rhs;

    public EqualsFunction(Function<?> lhs, Function<?> rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Boolean apply(Map<String, Object> scope) {
        Object lhs = this.lhs.apply(scope);
        Object rhs = this.rhs.apply(scope);
        return lhs.equals(rhs);
    }
}
