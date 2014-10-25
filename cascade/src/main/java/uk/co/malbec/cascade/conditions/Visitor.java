package uk.co.malbec.cascade.conditions;


public interface Visitor {
    public void visit(AndPredicate andPredicate);
    public void visit(OrPredicate orPredicate);
    public void visit(WithStepPredicate withStepPredicate);
    public void visit(NotPredicate notPredicate);
}
