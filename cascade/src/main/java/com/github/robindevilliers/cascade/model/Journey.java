package com.github.robindevilliers.cascade.model;


import com.github.robindevilliers.cascade.Scenario;
import org.junit.runner.Description;

import java.util.List;

public class Journey {

    private List<Scenario> steps;

    private Class<?> controlClass;

    private Description description;

    private String id;

    private String name;

    public Journey(List<Scenario> steps, Class<?> controlClass) {
        this.steps = steps;
        this.controlClass = controlClass;
    }

    public void init(int index) {
        id = "Test[" + index + "]";
        StringBuilder nameBuffer = new StringBuilder();
        nameBuffer.append(id).append(" ");
        for (Scenario scenario : steps) {
            nameBuffer.append(" ").append(scenario.getSimpleName()).append(" ");
        }
        name = nameBuffer.toString();
        description = Description.createTestDescription(controlClass, name);
    }

    public List<Scenario> getSteps() {
        return steps;
    }

    public Description getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
