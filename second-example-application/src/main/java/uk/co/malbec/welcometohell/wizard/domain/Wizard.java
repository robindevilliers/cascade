package uk.co.malbec.welcometohell.wizard.domain;

import java.util.ArrayList;
import java.util.List;

public class Wizard {

    private String id;

    private String start;

    private String rem;

    private String em;

    private List<View> views = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<View> getViews() {
        return views;
    }

    public View findView(String viewId) {
        return views
                .stream()
                .filter(v -> v.getId().equals(viewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid view id: " + viewId));
    }

    public View getStartView() {
        return findView(start);
    }
}
