package com.github.robindevilliers.welcometohell.wizard.expression.function;

import com.github.robindevilliers.welcometohell.wizard.expression.Function;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

public class OrFunction implements Function<Boolean> {

    private Deque<Function<Boolean>> predicates = new LinkedList<>();

    public OrFunction(Function<Boolean> lhs, Function<Boolean> rhs ){
        predicates.add(lhs);
        predicates.add(rhs);
    }

    public void add(Function<Boolean> predicate) {
        predicates.add(predicate);
    }

    public Function<Boolean> getFirst() {
        if (predicates.isEmpty()) {
            return null;
        } else {
            return predicates.getFirst();
        }
    }

    public Function<Boolean> removeFirst() {
        return predicates.removeFirst();
    }

    @Override
    public Boolean apply(Map<String, Object> scope) {
        for (Function<Boolean> predicate : predicates) {
            if (predicate.apply(scope)) {
                return true;
            }
        }
        return false;
    }
}
