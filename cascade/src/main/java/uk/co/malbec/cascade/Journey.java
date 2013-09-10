package uk.co.malbec.cascade;


import org.junit.runner.Description;

import java.util.List;

public class Journey {
    
    private List<Class> steps;

    private Description description;

    public Journey(List<Class> steps){
        this.steps = steps;
    }
    
    public List<Class> getSteps(){
        return steps;
    }


    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void generateDescription(Class testClass){
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
        description = Description.createTestDescription(testClass, buffer.toString());
    }
}
