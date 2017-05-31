package uk.co.malbec.cascade.conditions;


public class StepAtPredicate implements Predicate {

    private int index;
    private Class step;

    public StepAtPredicate(int index, Class step) {
        this.index = index;
        this.step = step;
    }

    public int getIndex() {
        return index;
    }

    public Class getStep() {
        return step;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
