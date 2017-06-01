package com.github.robindevilliers.cascade.conditions;

public interface Visitable {
    void accept(Visitor visitor);
}
