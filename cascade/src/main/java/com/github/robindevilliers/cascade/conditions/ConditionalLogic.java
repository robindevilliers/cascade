package com.github.robindevilliers.cascade.conditions;


import com.github.robindevilliers.cascade.Scenario;

import java.util.List;

public class ConditionalLogic {

    public boolean matches (Predicate predicate, List<Scenario> steps){
        EvaluatingVisitor evaluatingVisitor = new EvaluatingVisitor(steps);
        predicate.accept(evaluatingVisitor);
        return evaluatingVisitor.getResult();
    }
}
