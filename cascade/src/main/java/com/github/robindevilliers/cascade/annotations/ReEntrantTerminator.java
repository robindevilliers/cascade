package com.github.robindevilliers.cascade.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ReEntrantTerminator {
    int value() default 2;
}
