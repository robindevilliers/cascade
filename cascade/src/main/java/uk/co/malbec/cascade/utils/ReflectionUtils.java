package uk.co.malbec.cascade.utils;

import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Supplies;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.modules.reporter.StateRenderingStrategy;
import uk.co.malbec.cascade.modules.reporter.TransitionRenderingStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReflectionUtils {

    public static <T extends Annotation> void invokeStaticAnnotatedMethod(Class<T> annotationClass, Class subject, Class[] argumentTypes, Object[] arguments) {
        Method annotatedMethod = findStaticAnnotatedMethod(annotationClass, subject);
        if (annotatedMethod != null) {
            try {
                if (Arrays.equals(annotatedMethod.getParameterTypes(), argumentTypes)) {
                    annotatedMethod.invoke(null, arguments);
                } else {
                    annotatedMethod.invoke(null);
                }
            } catch (InvocationTargetException e) {
                throw new CascadeException(String.format("Invocation target exception executing %s method on class: %s", annotationClass.getName(), subject.getClass().toString()), e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new CascadeException(String.format("Illegal access exception trying to execute %s method on step class: %s", annotationClass.getName(), subject.getClass().toString()), e);
            }
        }
    }

    public static <T extends Annotation> void invokeAnnotatedMethod(Class<T> annotationClass, Object subject, Class[] argumentTypes, Object[] arguments) {
        Method annotatedMethod = findAnnotatedMethod(annotationClass, subject);
        if (annotatedMethod != null) {

            try {
                if (Arrays.equals(annotatedMethod.getParameterTypes(), argumentTypes)) {
                    annotatedMethod.invoke(subject, arguments);
                } else {
                    annotatedMethod.invoke(subject);
                }
            } catch (InvocationTargetException e) {
                throw new CascadeException(String.format("Invocation target exception executing %s method on class: %s", annotationClass.getName(), subject.getClass().toString()), e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new CascadeException(String.format("Illegal access exception trying to execute %s method on step class: %s", annotationClass.getName(), subject.getClass().toString()), e);
            }
        }
    }

    public static <T extends Annotation> void invokeAnnotatedMethod(Class<T> annotationClass, Object subject) {
        Method annotatedMethod = findAnnotatedMethod(annotationClass, subject);
        if (annotatedMethod != null) {

            try {
                annotatedMethod.invoke(subject);
            } catch (InvocationTargetException e) {
                throw new CascadeException(String.format("Invocation target exception executing %s method on class: %s", annotationClass.getName(), subject.getClass().toString()), e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new CascadeException(String.format("Illegal access exception trying to execute %s method on step class: %s", annotationClass.getName(), subject.getClass().toString()), e);
            }
        }
    }

    public static <T extends Annotation> Method findStaticAnnotatedMethod(Class<T> annotationClass, Class subject) {

        for (Method method : subject.getMethods()) {
            T annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                return method;
            }
        }
        return null;
    }

    public static <T extends Annotation> Method findAnnotatedMethod(Class<T> annotationClass, Object subject) {

        for (Method method : subject.getClass().getMethods()) {
            T annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                return method;
            }
        }
        return null;
    }

    public static <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass, Class subject) {

        for (Method method : subject.getMethods()) {
            T annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }


    public static void collectSuppliedFields(Object subject, Map<String, Scope> scope) {
        for (Field field : subject.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                Supplies supplies = field.getAnnotation(Supplies.class);
                if (supplies != null) {
                    Object value = getFieldValue(field, subject);

                    if (value != null) {

                        StateRenderingStrategy stateRenderingStrategy = null;

                        if (supplies.stateRenderer() != Object.class) {
                            try {
                                stateRenderingStrategy = (StateRenderingStrategy) newInstance(supplies.stateRenderer(), "stateRenderer");
                            } catch (ClassCastException e) {
                                throw new CascadeException("Class supplied as a StateRenderingStrategy is not of instance StateRenderingStrategy in " + subject.getClass());
                            }
                        }

                        TransitionRenderingStrategy transitionRenderingStrategy = null;

                        if (supplies.transitionRenderer() != Object.class) {
                            try {
                                transitionRenderingStrategy = (TransitionRenderingStrategy) newInstance(supplies.transitionRenderer(), "transitionRenderer");
                            } catch (ClassCastException e) {
                                throw new CascadeException("Class supplied as a TransitionRenderingStrategy is not of instance TransitionRenderingStrategy in " + subject.getClass());
                            }
                        }

                        scope.put(field.getName(), new Scope(value, stateRenderingStrategy, transitionRenderingStrategy));
                    }
                }
            }
        }
    }

    public static void collectStaticSuppliedFields(Class subject, Map<String, Scope> scope) {
        for (Field field : subject.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                Supplies supplies = field.getAnnotation(Supplies.class);
                if (supplies != null) {

                    Object value = getFieldValue(field, subject);

                    if (value != null) {

                        StateRenderingStrategy stateRenderingStrategy = null;

                        if (supplies.stateRenderer() != Object.class) {
                            try {
                                stateRenderingStrategy = (StateRenderingStrategy) newInstance(supplies.stateRenderer(), "stateRenderer");
                            } catch (ClassCastException e) {
                                throw new CascadeException("Class supplied as a StateRenderingStrategy is not of instance StateRenderingStrategy in " + subject.getClass());
                            }
                        }

                        TransitionRenderingStrategy transitionRenderingStrategy = null;

                        if (supplies.transitionRenderer() != Object.class) {
                            try {
                                transitionRenderingStrategy = (TransitionRenderingStrategy) newInstance(supplies.transitionRenderer(), "transitionRenderer");
                            } catch (ClassCastException e) {
                                throw new CascadeException("Class supplied as a TransitionRenderingStrategy is not of instance TransitionRenderingStrategy in " + subject.getClass());
                            }
                        }

                        scope.put(field.getName(), new Scope(value, stateRenderingStrategy, transitionRenderingStrategy));
                    }
                }
            }
        }
    }

    public static void injectStaticDemandedFields(Class subject, Map<String, Scope> scope) {
        for (Field field : subject.getDeclaredFields()) {
            Demands demands = field.getAnnotation(Demands.class);
            if (demands == null) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                String fieldName = field.getName();
                setFieldValue(field, subject, scope.get(fieldName) != null ? scope.get(fieldName).getValue() : null);
            }
        }
    }

    public static void injectDemandedFields(Object subject, Map<String, Scope> scope) {
        for (Field field : subject.getClass().getDeclaredFields()) {
            Demands demands = field.getAnnotation(Demands.class);
            if (demands == null) {
                continue;
            }

            String fieldName = field.getName();
            setFieldValue(field, subject, scope.get(fieldName) != null ? scope.get(fieldName).getValue() : null);
        }
    }

    public static Object newInstance(Class clazz, String name) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException e) {
            throw new CascadeException(String.format("Illegal access exception trying to instantiate %s class: %s", name, clazz.toString()), e);
        } catch (InstantiationException e) {
            throw new CascadeException(String.format("Instantiation exception trying to instantiate %s class: %s", name, clazz.toString()), e);
        }
    }

    public static Object getValueOfFieldAnnotatedWith(Object subject, Class annotationClass) {
        for (Field field : subject.getClass().getDeclaredFields()) {

            Annotation annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                return getFieldValue(field, subject);
            }
        }
        return null;
    }

    public static <T> T[] getValuesOfFieldsAnnotatedWith(Object subject, Class annotationClass, Class<T> expectedClass) {
        List<T> results = new ArrayList<>();
        for (Field field : subject.getClass().getDeclaredFields()) {

            Annotation annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                results.add((T) getFieldValue(field, subject));
            }
        }
        return results.toArray((T[]) Array.newInstance(expectedClass, 0));
    }

    public static Object getFieldValue(Field field, Object instance) {
        field.setAccessible(true);
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            //can't happen
        }
        return null;
    }

    public static void setFieldValue(Field field, Object instance, Object value) {
        field.setAccessible(true);

        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            //can't happen
        } catch (IllegalArgumentException e) {
            throw new CascadeException("Field (" + field.getName() + ") does not match type of value that is being injected in step: " + instance.getClass());

        }
    }
}
