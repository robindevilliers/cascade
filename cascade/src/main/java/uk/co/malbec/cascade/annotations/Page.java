package uk.co.malbec.cascade.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Page {
    Class[] value() default Null.class;
    public static class Null {}
}
