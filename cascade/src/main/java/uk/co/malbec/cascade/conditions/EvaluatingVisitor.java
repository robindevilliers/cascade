package uk.co.malbec.cascade.conditions;

import uk.co.malbec.cascade.Edge;
import uk.co.malbec.cascade.Thig;

import java.util.ArrayList;
import java.util.List;

public class EvaluatingVisitor implements Visitor {

    private boolean result;
    private List<Class> scenarioClasses;

    public EvaluatingVisitor(List<Thig> thigs){
        scenarioClasses = new ArrayList<>();
        for (Thig thig : thigs){
            scenarioClasses.add(thig.getCls());
        }
    }

    @Override
    public void visit(AndPredicate andPredicate) {
        for (Predicate predicate : andPredicate.getPredicates()){
            predicate.accept(this);
            if (!result){
                return;
            }
        }
        this.result = true;
    }

    @Override
    public void visit(OrPredicate orPredicate) {
        for (Predicate predicate : orPredicate.getPredicates()){
            predicate.accept(this);
            if (result){
                return;
            }
        }
        result = false;
    }

    @Override
    public void visit(WithStepPredicate withStepPredicate) {
        //TODO - add code so that I can define a step interface here as well as a concrete class.
        result = scenarioClasses.contains(withStepPredicate.getStep());
    }

    @Override
    public void visit(StepAtPredicate stepAtPredicate) {
        result = scenarioClasses.size() > stepAtPredicate.getIndex() && scenarioClasses.get(stepAtPredicate.getIndex()).equals(stepAtPredicate.getStep());
    }

    @Override
    public void visit(NotPredicate notPredicate) {
        notPredicate.getPredicate().accept(this);
        result = !result;
    }

    public boolean getResult(){
        return result;
    }
}
