package uk.co.malbec.cascade.modules.scanner;


import org.reflections.Reflections;
import uk.co.malbec.cascade.modules.ClasspathScanner;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ReflectionsClasspathScanner implements ClasspathScanner {

    private Reflections reflections;

    @Override
    public void initialise(String path) {
        reflections = new Reflections(path);
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    @Override
    public Set<Class> getSubTypesOf(Class type) {
        return reflections.getSubTypesOf(type);
    }
}
