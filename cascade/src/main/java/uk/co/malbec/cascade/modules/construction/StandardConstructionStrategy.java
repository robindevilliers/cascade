package uk.co.malbec.cascade.modules.construction;


import uk.co.malbec.cascade.annotations.Clear;
import uk.co.malbec.cascade.annotations.Given;
import uk.co.malbec.cascade.annotations.Setup;
import uk.co.malbec.cascade.annotations.Teardown;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.ConstructionStrategy;
import uk.co.malbec.cascade.utils.Reference;

import java.util.*;

import static uk.co.malbec.cascade.utils.ReflectionUtils.*;

public class StandardConstructionStrategy implements ConstructionStrategy {

    @Override
    public void setup(Class<?> controlClass, Journey journey, Reference<Object> control, Reference<List<Object>> steps) {
        Map<String, Object> scope = new HashMap<String, Object>();

        control.set(newInstance(controlClass, "control"));

        steps.set(new ArrayList<Object>());
        Map<Class, Object> singletons = new HashMap<Class, Object>();
        for (Class clazz : journey.getSteps()) {
            if (singletons.get(clazz) == null) {
                singletons.put(clazz, newInstance(clazz, "step"));
            }
            steps.get().add(singletons.get(clazz));
        }

        collectSuppliedFields(control.get(), scope);

        for (Object step : steps.get()) {
            collectSuppliedFields(step, scope);
        }

        for (Object step : steps.get()) {
            injectDemandedFields(step, scope);
        }


        Set<Object> alreadyInitialised = new HashSet<Object>();
        for (Object step : steps.get()) {
            if (!alreadyInitialised.contains(step)) {
                invokeAnnotatedMethod(Given.class, step);
                alreadyInitialised.add(step);
            }
        }

        for (Object step : steps.get()) {
            collectSuppliedFields(step, scope);
        }

        injectDemandedFields(control.get(), scope);

        invokeAnnotatedMethod(Setup.class, control.get());

        collectSuppliedFields(control.get(), scope);

        for (Object step : steps.get()) {
            injectDemandedFields(step, scope);
        }
    }

    @Override
    public void tearDown(Reference<Object> control, Reference<List<Object>> steps) {
        for (Object step : steps.get()) {
            invokeAnnotatedMethod(Clear.class, step);
        }

        invokeAnnotatedMethod(Teardown.class, control.get());
    }
}
