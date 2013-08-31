package uk.co.test.cascade.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Step {
    Class[] value() default Null.class;
    
    

    public static class Null {}
}