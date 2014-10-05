package uk.co.malbec.cascade.model;


import org.junit.runner.Description;

import java.util.List;

public class Journey {
    
    private List<Class> steps;

    private Class<?> controlClass;

    private Description description;
    
    private String name;

    public Journey(List<Class> steps, Class<?> controlClass){
        this.steps = steps;
        this.controlClass = controlClass;
    }

    public void init(){
        StringBuffer buffer = new StringBuffer();
        for (Class<?> cls: steps){
            uk.co.malbec.cascade.annotations.Description description  = cls.getAnnotation(uk.co.malbec.cascade.annotations.Description.class);
            if (description != null){
                buffer.append(description.value());
            } else {
                buffer.append(" ");
                buffer.append(cls.toString());
                buffer.append(" ");
            }
        }
        name = buffer.toString();
        description = Description.createTestDescription(controlClass, name);
    }
    
    public List<Class> getSteps(){
        return steps;
    }

    public Description getDescription() {
        return description;
    }
    
    public String getName(){
        return name;
    }
}
