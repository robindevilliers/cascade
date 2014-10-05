package uk.co.malbec.cascade;


import uk.co.malbec.cascade.conditions.*;

import java.util.List;

public class ConditionalLogic {

    public boolean matches (Predicate predicate, List<Class> steps){
        EvaluatingVisitor evaluatingVisitor = new EvaluatingVisitor(steps);
        predicate.accept(evaluatingVisitor);
        return evaluatingVisitor.getResult();
    }
}
