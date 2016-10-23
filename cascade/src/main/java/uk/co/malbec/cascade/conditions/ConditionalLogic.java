package uk.co.malbec.cascade.conditions;


import uk.co.malbec.cascade.Edge;
import uk.co.malbec.cascade.Thig;

import java.util.List;

public class ConditionalLogic {

    public boolean matches (Predicate predicate, List<Thig> steps){
        EvaluatingVisitor evaluatingVisitor = new EvaluatingVisitor(steps);
        predicate.accept(evaluatingVisitor);
        return evaluatingVisitor.getResult();
    }
}
