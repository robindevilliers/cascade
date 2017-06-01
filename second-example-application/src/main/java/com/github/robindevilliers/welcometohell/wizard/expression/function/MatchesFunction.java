package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.util.Map;

public class MatchesFunction implements Function<Boolean> {

    private Function<?> lhs;
    private Function<?> rhs;

    public MatchesFunction(Function<?> lhs, Function<?> rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Boolean apply(Map<String, Object> scope) {
        Object lhs = this.lhs.apply(scope);
        Object rhs = this.rhs.apply(scope);

        if (lhs instanceof String && rhs instanceof String) {
            String string = (String) lhs;
            String pattern = (String) rhs;

            return string.matches(pattern);
        } else {
            throw new RuntimeException("Invalid expression.  Unsupported types.");
        }


    }
}
