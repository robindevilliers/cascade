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

    public static <T extends Annotation> void invokeAnnotatedMethod(Class<T> annotationClass, Object subject) throws InvocationTargetException, IllegalAccessException {
        Method annotatedMethod = findAnnotatedMethod(annotationClass, subject);
        if (annotatedMethod != null) {

                annotatedMethod.invoke(subject);

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

    public static void collectSuppliedFields(Object subject, Map<String, Object> scope) throws IllegalAccessException {
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

    public static void injectDemandedFields(Object subject, Map<String, Object> scope) throws IllegalAccessException {
        for (Field field : subject.getClass().getDeclaredFields()) {
            Demands demands = field.getAnnotation(Demands.class);
            if (demands == null) {
                continue;
            }

            String fieldName = field.getName();
            setFieldValue(field, subject, scope.get(fieldName));
        }
    }

    public static Object newInstance(Class clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

    public static Object getFieldValue(Field field, Object instance) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(instance);
    }

    public static void setFieldValue(Field field, Object instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);

        field.set(instance, value);

    }
}
