package com.github.robindevilliers.cascade;

import com.github.robindevilliers.cascade.annotations.*;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class Scenario {

    private Class<?> clazz;

    private Class<?> stateClazz;

    private boolean isTerminator;

    private boolean isReEntrantTerminator;

    private boolean isRepeatable;

    private int reEntrantCount = 0;

    private Class[] steps;

    public Scenario(Class<?> clazz, Class<?> stateClazz) {
        this.clazz = clazz;
        this.stateClazz = stateClazz;
        steps = findAnnotation(Step.class, clazz).value();
        isTerminator = findAnnotation(Terminator.class, clazz) != null;

        isReEntrantTerminator = false;

        ReEntrantTerminator reEntrantTerminator = findAnnotation(ReEntrantTerminator.class, clazz);
        if (reEntrantTerminator != null) {
            isReEntrantTerminator = true;
            reEntrantCount = reEntrantTerminator.value();
            isRepeatable = true;
        }

        SoftTerminator softTerminator = findAnnotation(SoftTerminator.class, clazz);
        if (softTerminator != null) {
            isReEntrantTerminator = true;
            reEntrantCount = 100;
            isRepeatable = true;
        }

        Repeatable repeatable = findAnnotation(Repeatable.class, clazz);
        if (repeatable != null) {
            isRepeatable = true;
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Class<?> getStateClazz() {
        return stateClazz;
    }

    public String getName() {
        return clazz.getName();
    }

    public String getSimpleName(){
        String[] parts = clazz.toString().split("[.]");
       return parts[parts.length - 1];
    }

    public String getNarrative(){
        return Optional.ofNullable(clazz.getAnnotation(Narrative.class)).map(Narrative::value).orElse(getSimpleName());
    }

    public boolean isTerminator() {
        return isTerminator;
    }

    public boolean isReEntrantTerminator() {
        return isReEntrantTerminator;
    }

    public boolean isRepeatable(){
        return isRepeatable;
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

        return clazz != null ? clazz.equals(scenario.clazz) : scenario.clazz == null;
    }

    @Override
    public int hashCode() {
        return clazz != null ? clazz.hashCode() : 0;
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
