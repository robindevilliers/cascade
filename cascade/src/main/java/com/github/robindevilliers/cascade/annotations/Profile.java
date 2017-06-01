package com.github.robindevilliers.cascade.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Profile {
    String value();
    String name() default "CASCADE_PROFILE";
}
