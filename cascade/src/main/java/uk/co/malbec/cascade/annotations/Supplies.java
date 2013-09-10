package uk.co.malbec.cascade.annotations;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Supplies {
}
