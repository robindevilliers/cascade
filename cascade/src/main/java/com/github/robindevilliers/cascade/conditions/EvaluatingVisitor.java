package com.github.robindevilliers.cascade.conditions;

import com.github.robindevilliers.cascade.Scenario;

import java.util.ArrayList;
import java.util.List;

public class EvaluatingVisitor implements Visitor {

    private boolean result;
    private List<Class<?>> scenarioClasses;

    public EvaluatingVisitor(List<Scenario> scenarios){
        scenarioClasses = new ArrayList<>();
        for (Scenario scenario : scenarios){
            scenarioClasses.add(scenario.getClazz());
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
        result = scenarioClasses
                .stream()
                .anyMatch(c -> withStepPredicate.getStep().isAssignableFrom(c));
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

    @Override
    public void visit(StepIsCoupledWithPredicate stepFollowedByPredicate) {

        result = false;
        boolean latch = false;
        for (Class<?> cls: scenarioClasses){

            if (!latch && stepFollowedByPredicate.getFollowedBy().isAssignableFrom(cls)){
                result = false;
                break;
            }

            if (latch && stepFollowedByPredicate.getFollowedBy().isAssignableFrom(cls)) {
                result = true;
                latch = false;
            }

            if (!latch && stepFollowedByPredicate.getStep().isAssignableFrom(cls)){
                latch = true;
            }
        }
    }

    public boolean getResult(){
        return result;
    }
}
