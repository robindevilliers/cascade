package uk.co.malbec.cascade.annotations;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Supplies {
    Class stateRenderer() default Object.class;
    Class transitionRenderer() default Object.class;
}
