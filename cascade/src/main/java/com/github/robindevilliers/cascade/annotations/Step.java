package com.github.robindevilliers.cascade.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Step {
    Class[] value() default Null.class;

    class Null {
    }
}