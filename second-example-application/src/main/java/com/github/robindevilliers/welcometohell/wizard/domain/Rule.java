package com.github.robindevilliers.welcometohell.wizard.domain;


import com.github.robindevilliers.welcometohell.wizard.domain.types.Route;

import java.util.ArrayList;
import java.util.List;

public class Rule implements Route {

    private String condition;

    private String view;

    private List<Variable> variables = new ArrayList<>();

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getViewId() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public List<Variable> getVariables() {
        return variables;
    }
}
