package com.github.robindevilliers.welcometohell.wizard.domain;

import java.util.ArrayList;
import java.util.List;

public class RouteMappings {

    private List<Rule> rules = new ArrayList<>();

    private Default def;

    public Default getDefault() {
        return def;
    }

    public void setDefault(Default def) {
        this.def = def;
    }

    public List<Rule> getRules() {
        return rules;
    }
}
