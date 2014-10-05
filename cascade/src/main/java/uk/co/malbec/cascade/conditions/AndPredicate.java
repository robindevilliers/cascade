package uk.co.malbec.cascade.conditions;

public class AndPredicate implements Predicate {

    private Predicate[] predicates;

    public AndPredicate(Predicate[] predicates){
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