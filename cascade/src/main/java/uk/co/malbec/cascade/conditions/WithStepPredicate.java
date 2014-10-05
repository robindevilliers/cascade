package uk.co.malbec.cascade.conditions;


public class WithStepPredicate implements Predicate {

    private Class step;

    public WithStepPredicate(Class step) {
        this.step = step;
    }

    public Class getStep() {
        return step;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WithStepPredicate that = (WithStepPredicate) o;

        if (!step.equals(that.step)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return step.hashCode();
    }
}
