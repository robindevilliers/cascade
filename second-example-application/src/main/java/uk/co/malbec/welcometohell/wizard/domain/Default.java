package uk.co.malbec.welcometohell.wizard.domain;

import uk.co.malbec.welcometohell.wizard.domain.types.Route;

import java.util.ArrayList;
import java.util.List;

public class Default implements Route {

    private String view;

    private List<Variable> variables = new ArrayList<>();

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
