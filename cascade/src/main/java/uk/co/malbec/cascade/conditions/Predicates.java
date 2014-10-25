package uk.co.malbec.cascade.conditions;

public class Predicates {

    public static Predicate withStep(Class step) {
        return new WithStepPredicate(step);
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

