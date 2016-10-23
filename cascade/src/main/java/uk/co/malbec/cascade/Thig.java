package uk.co.malbec.cascade;

import java.lang.annotation.Annotation;

public class Thig {

    private Class cls;

    public Thig(Class cls){
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getName() {
        return cls.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Thig thig = (Thig) o;

        if (cls != null ? !cls.equals(thig.cls) : thig.cls != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cls != null ? cls.hashCode() : 0;
    }


    protected static <T extends Annotation> T findAnnotation(Class<T> annotationClass, Class<?> subject) {
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
