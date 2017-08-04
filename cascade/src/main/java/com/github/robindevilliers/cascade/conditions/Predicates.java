package com.github.robindevilliers.cascade.conditions;

public class Predicates {

    public static Predicate withStep(Class step) {
        return new WithStepPredicate(step);
    }

    public static Predicate stepIsCoupledWith(Class step, Class followingStep) {
        return new StepIsCoupledWithPredicate(step, followingStep);
    }

    public static Predicate stepAt(int i, Class step) {
        return new StepAtPredicate(i, step);
    }

    public static Predicate or(Predicate... predicates) {
        return new OrPredicate(predicates);
    }

    public static Predicate and(Predicate... predicates) {
        return new AndPredicate(predicates);
    }

    public static Predicate not(Predicate predicate){
        return new NotPredicate((predicate));
    }
}

