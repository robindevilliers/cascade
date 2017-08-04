package com.github.robindevilliers.cascade.conditions;


public interface Visitor {
    void visit(AndPredicate andPredicate);
    void visit(OrPredicate orPredicate);
    void visit(WithStepPredicate withStepPredicate);
    void visit(StepAtPredicate stepAtPredicate);
    void visit(NotPredicate notPredicate);
    void visit(StepIsCoupledWithPredicate stepFollowedByPredicate);
}
