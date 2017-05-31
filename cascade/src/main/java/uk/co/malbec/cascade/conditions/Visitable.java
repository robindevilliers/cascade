package uk.co.malbec.cascade.conditions;

public interface Visitable {
    void accept(Visitor visitor);
}
