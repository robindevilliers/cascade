package uk.co.malbec.cascade.conditions;

import java.util.List;

public class EvaluatingVisitor implements Visitor {

    private boolean result;
    private List<Class> steps;

    public EvaluatingVisitor(List<Class> steps){
        this.steps = steps;
    }

    @Override
    public void visit(AndPredicate andPredicate) {
        boolean result = true;
        for (Predicate predicate : andPredicate.getPredicates()){
            EvaluatingVisitor visitor = new EvaluatingVisitor(steps);
            predicate.accept(visitor);
            if (!visitor.getResult()){
                result = false;
                break;
            }
        }
        this.result = result;
    }

    @Override
    public void visit(OrPredicate orPredicate) {
        boolean result = false;
        for (Predicate predicate : orPredicate.getPredicates()){
            EvaluatingVisitor visitor = new EvaluatingVisitor(steps);
            predicate.accept(visitor);
            if (visitor.getResult()){
                result = true;
                break;
            }
        }
        this.result = result;
    }

    @Override
    public void visit(WithStepPredicate withStepPredicate) {
        //TODO - add code so that I can define a step interface here as well as a concrete class.
        result = steps.contains(withStepPredicate.getStep());
    }

    public boolean getResult(){
        return result;
    }
}
