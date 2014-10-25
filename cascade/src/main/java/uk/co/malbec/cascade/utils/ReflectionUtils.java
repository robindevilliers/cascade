package uk.co.malbec.cascade.utils;

import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Supplies;
import uk.co.malbec.cascade.exception.CascadeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUtils {

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

    public static <T extends Annotation> Method findAnnotatedMethod(Class<T> annotationClass, Object subject) {

        for (Method method : subject.getClass().getMethods()) {
            T annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                return method;
            }
        }
        return null;
    }

    public static void collectSuppliedFields(Object subject, Map<String, Object> scope) {
        for (Field field : subject.getClass().getDeclaredFields()) {
            Supplies supplies = field.getAnnotation(Supplies.class);
            if (supplies != null) {
                Object value = getFieldValue(field, subject);

                if (value != null) {
                    scope.put(field.getName(), value);
                }
            }
        }
    }

    public static void injectDemandedFields(Object subject, Map<String, Object> scope) {
        for (Field field : subject.getClass().getDeclaredFields()) {
            Demands demands = field.getAnnotation(Demands.class);
            if (demands == null) {
                continue;
            }

            String fieldName = field.getName();
            setFieldValue(field, subject, scope.get(fieldName));
        }
    }

    public static Object newInstance(Class clazz, String name)  {
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
        }
    }
}
