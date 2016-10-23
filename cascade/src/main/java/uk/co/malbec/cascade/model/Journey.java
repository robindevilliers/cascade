package uk.co.malbec.cascade.model;


import org.junit.runner.Description;
import uk.co.malbec.cascade.Edge;
import uk.co.malbec.cascade.Thig;
import uk.co.malbec.cascade.Vertex;

import java.util.List;

public class Journey {

    private List<Thig> trail;

    private Class<?> controlClass;

    private Description description;

    private String name;

    public Journey(List<Thig> trail, Class<?> controlClass) {
        this.trail = trail;
        this.controlClass = controlClass;
    }

    public void init(int index) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Test[").append(index).append("] ");

        for (Thig thig : trail) {
            uk.co.malbec.cascade.annotations.Description description = thig.getCls().getAnnotation(uk.co.malbec.cascade.annotations.Description.class);
            if (description != null) {
                buffer.append(description.value());
            } else {
                buffer.append(" ");
                String[] parts = thig.getCls().toString().split("[.]");
                buffer.append(parts[parts.length - 1]);
                buffer.append(" ");
            }
        }
        name = buffer.toString();
        description = Description.createTestDescription(controlClass, name);
    }

    public List<Thig> getTrail() {
        return trail;
    }

    public Description getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
