package com.github.robindevilliers.cascade.modules;


import java.lang.annotation.Annotation;
import java.util.Set;

public interface ClasspathScanner {

    void initialise(String path);

    Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation);

    Set<Class> getSubTypesOf(final Class type);
}
