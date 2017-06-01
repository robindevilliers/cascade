package com.github.robindevilliers.cascade.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface When {
    String Null = "";
}
