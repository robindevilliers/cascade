package uk.co.malbec.cascade.annotations;

import uk.co.malbec.cascade.Completeness;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CompletenessLevel {
    Completeness value();
}