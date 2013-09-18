package uk.co.malbec.cascade.utils;

import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Supplies;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUtils {

    public static <T extends Annotation> void invokeAnnotatedMethod(Class<T> annotationClass, Object subject) {
        Method annotatedMethod = findAnnotatedMethod(annotationClass, subject);
        if (annotatedMethod != null) {
            try {
                annotatedMethod.invoke(subject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static <T extends Annotation> Method findAnnotatedMethod(Class<T> annotationClass, Object subject){

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

    public static Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldValue(Field field, Object instance) {
        field.setAccessible(true);
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Field field, Object instance, Object value) {
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
