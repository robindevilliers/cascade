package uk.co.malbec.cascade.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Then {
    String scenarioId() default Null;

    String stateId() default Null;

    String Null = "";
}
