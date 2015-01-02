package uk.co.malbec.cascade.model;


import org.junit.runner.Description;
import uk.co.malbec.cascade.Scenario;

import java.util.List;

public class Journey {

    private List<Scenario> steps;

    private Class<?> controlClass;

    private Description description;

    private String name;

    public Journey(List<Scenario> steps, Class<?> controlClass) {
        this.steps = steps;
        this.controlClass = controlClass;
    }

    public void init(int index) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Test[").append(index).append("] ");
        for (Scenario scenario : steps) {
            uk.co.malbec.cascade.annotations.Description description = scenario.getCls().getAnnotation(uk.co.malbec.cascade.annotations.Description.class);
            if (description != null) {
                buffer.append(description.value());
            } else {
                buffer.append(" ");
                String[] parts = scenario.getCls().toString().split("[.]");
                buffer.append(parts[parts.length - 1]);
                buffer.append(" ");
            }
        }
        name = buffer.toString();
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
}
