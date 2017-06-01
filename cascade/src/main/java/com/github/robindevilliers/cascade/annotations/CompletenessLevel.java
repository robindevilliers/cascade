package com.github.robindevilliers.cascade.annotations;

import com.github.robindevilliers.cascade.Completeness;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CompletenessLevel {
    Completeness value();
}