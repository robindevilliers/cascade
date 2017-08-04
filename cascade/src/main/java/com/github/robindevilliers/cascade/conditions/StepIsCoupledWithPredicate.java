package com.github.robindevilliers.cascade.conditions;

public class StepIsCoupledWithPredicate implements Predicate {

    private Class step;
    private Class followedBy;

    public StepIsCoupledWithPredicate(Class step, Class followedBy) {
        this.step = step;
        this.followedBy = followedBy;
    }

    public Class<?> getStep() {
        return step;
    }

    public Class<?> getFollowedBy() {
        return followedBy;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StepIsCoupledWithPredicate that = (StepIsCoupledWithPredicate) o;

        if (!step.equals(that.step)) return false;
        return followedBy.equals(that.followedBy);
    }

    @Override
    public int hashCode() {
        return step.hashCode();
    }
}
