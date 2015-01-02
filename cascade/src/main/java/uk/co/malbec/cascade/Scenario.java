package uk.co.malbec.cascade;

import uk.co.malbec.cascade.annotations.*;

import java.lang.annotation.Annotation;

public class Scenario {

    private Class cls;

    private boolean isTerminator;

    private boolean isReEntrantTerminator;

    private int reEntrantCount = 0;

    private Class[] steps;

    public Scenario(Class cls) {
        this.cls = cls;
        steps = findAnnotation(Step.class, cls).value();

        isTerminator = findAnnotation(Terminator.class, cls) != null;

        ReEntrantTerminator reEntrantTerminator = findAnnotation(ReEntrantTerminator.class, cls);
        if (reEntrantTerminator != null) {
            isReEntrantTerminator = true;
            reEntrantCount = reEntrantTerminator.value();
        } else {
            isReEntrantTerminator = false;
        }

    }

    public Class<?> getCls() {
        return cls;
    }

    public String getName() {
        return cls.getName();
    }

    public boolean isTerminator() {
        return isTerminator;
    }

    public boolean isReEntrantTerminator() {
        return isReEntrantTerminator;
    }

    public long getReEntrantCount(){
        return reEntrantCount;
    }

    public Class[] getSteps(){
        return steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scenario scenario = (Scenario) o;

        if (cls != null ? !cls.equals(scenario.cls) : scenario.cls != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cls != null ? cls.hashCode() : 0;
    }


    private <T extends Annotation> T findAnnotation(Class<T> annotationClass, Class<?> subject) {
        T step = subject.getAnnotation(annotationClass);
        if (step != null) {
            return step;
        }

        for (Class<?> i : subject.getInterfaces()) {
            step = i.getAnnotation(annotationClass);
            if (step != null) {
                return step;
            }
        }

        Class superClass = subject.getSuperclass();
        if (superClass != null) {
            return findAnnotation(annotationClass, superClass);
        }
        return null;
    }
}
