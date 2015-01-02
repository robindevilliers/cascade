package uk.co.malbec.cascade.conditions;


import uk.co.malbec.cascade.Scenario;

import java.util.List;

public class ConditionalLogic {

    public boolean matches (Predicate predicate, List<Scenario> steps){
        EvaluatingVisitor evaluatingVisitor = new EvaluatingVisitor(steps);
        predicate.accept(evaluatingVisitor);
        return evaluatingVisitor.getResult();
    }
}
