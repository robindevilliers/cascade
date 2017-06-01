package com.github.robindevilliers.cascade.conditions;

public class NotPredicate implements Predicate {

    private Predicate predicate;

    public NotPredicate(Predicate predicate){
        this.predicate = predicate;
    }

    public Predicate getPredicate(){
        return predicate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}