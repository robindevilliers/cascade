package uk.co.malbec.cascade.model;


import org.junit.runner.Description;

import java.util.List;

public class Journey {

    private List<Class> steps;

    private Class<?> controlClass;

    private Description description;

    private String name;

    public Journey(List<Class> steps, Class<?> controlClass) {
        this.steps = steps;
        this.controlClass = controlClass;
    }

    public void init(int index) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Test[").append(index).append("] ");
        for (Class<?> cls : steps) {
            uk.co.malbec.cascade.annotations.Description description = cls.getAnnotation(uk.co.malbec.cascade.annotations.Description.class);
            if (description != null) {
                buffer.append(description.value());
            } else {
                buffer.append(" ");
                String[] parts = cls.toString().split("[.]");
                buffer.append(parts[parts.length - 1]);
                buffer.append(" ");
            }
        }
        name = buffer.toString();
        description = Description.createTestDescription(controlClass, name);
    }

    public List<Class> getSteps() {
        return steps;
    }

    public Description getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
