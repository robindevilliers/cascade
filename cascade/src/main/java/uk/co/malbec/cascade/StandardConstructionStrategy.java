package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.Clear;
import uk.co.malbec.cascade.annotations.Given;
import uk.co.malbec.cascade.annotations.Setup;
import uk.co.malbec.cascade.annotations.Teardown;
import uk.co.malbec.cascade.utils.Reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.co.malbec.cascade.utils.ReflectionUtils.*;

public class StandardConstructionStrategy implements ConstructionStrategy {

    @Override
    public void setup(Class<?> controlClass, Journey journey, Reference<Object> control, Reference<List<Object>> steps) {
        Map<String, Object> scope = new HashMap<String, Object>();

        control.set(newInstance(controlClass));

        steps.set(new ArrayList<Object>());
        for (Class clazz : journey.getSteps()) {
            steps.get().add(newInstance(clazz));
        }

        collectSuppliedFields(control, scope);

        for (Object step : steps.get()) {
            collectSuppliedFields(step, scope);
        }

        for (Object step : steps.get()) {
            injectDemandedFields(step, scope);
        }

        for (Object step : steps.get()) {
            invokeAnnotatedMethod(Given.class, step);
        }

        for (Object step : steps.get()) {
            collectSuppliedFields(step, scope);
        }

        injectDemandedFields(control, scope);

        invokeAnnotatedMethod(Setup.class, controlClass);

        collectSuppliedFields(control, scope);

        for (Object step : steps.get()) {
            injectDemandedFields(step, scope);
        }
    }

    @Override
    public void tearDown( Reference<Object> control, Reference<List<Object>> steps) {

        for (Object step : steps.get()) {
            invokeAnnotatedMethod(Clear.class, step);
        }

        invokeAnnotatedMethod(Teardown.class, control.get());
    }
}
