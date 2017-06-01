package com.github.robindevilliers.cascade.conditions;

public class OrPredicate implements Predicate {

    private Predicate[] predicates;

    public OrPredicate(Predicate[] predicates){
        this.predicates = predicates;
    }

    public Predicate[] getPredicates(){
        return predicates;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
