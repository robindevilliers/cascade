package uk.co.malbec.cascade.util;


import java.lang.reflect.Field;

public class TestUtil {
    
    public static void setField(Object subject, String name, Object value){
        try {
            Field field = subject.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(subject, value);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getField(Object subject, String name){
        try {
            Field field = subject.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(subject);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
