package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndFunction implements Function<Boolean> {

    private List<Function<Boolean>> predicates = new ArrayList<>();

    public AndFunction(Function<Boolean> lhs, Function<Boolean> rhs ){
        predicates.add(lhs);
        predicates.add(rhs);
    }

    public void add(Function<Boolean> predicate) {
        predicates.add(predicate);
    }

    @Override
    public Boolean apply(Map<String, Object> scope) {
        for (Function<Boolean> predicate: predicates){
            if (!predicate.apply(scope)){
                return false;
            }
        }
        return true;
    }
}
