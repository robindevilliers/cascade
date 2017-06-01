package com.github.robindevilliers.welcometohell.wizard;

public enum Flavour {
    DEFAULT("default"), PRIMARY("primary"), INFO("info"), WARNING("warning"), DANGER("danger"), SUCCESS("success"), MUTED("muted");

    private String name;

    Flavour(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
