package uk.co.malbec.cascade.modules.construction;


import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.Scope;
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
    public Map<String, Scope> setup(Class<?> controlClass, Journey journey, Reference<Object> control, Reference<List<Object>> steps, Map<String, Scope> globalScope) {
        Map<String, Scope> scope = new HashMap<>(globalScope);

        control.set(newInstance(controlClass, "control"));

        steps.set(new ArrayList<>());
        Map<Scenario, Object> singletons = new HashMap<Scenario, Object>();
        for (Scenario scenario : journey.getSteps()) {
            singletons.computeIfAbsent(scenario, k -> newInstance(scenario.getClazz(), "step"));
            steps.get().add(singletons.get(scenario));
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

        invokeAnnotatedMethod(Setup.class, control.get(), new Class[]{Journey.class}, new Object[]{journey});

        collectSuppliedFields(control.get(), scope);

        for (Object step : steps.get()) {
            injectDemandedFields(step, scope);
        }

        return scope;
    }

    @Override
    public void tearDown(Reference<Object> control, Journey journey, Reference<List<Object>> steps) {
        for (Object step : steps.get()) {
            invokeAnnotatedMethod(Clear.class, step);
        }
        invokeAnnotatedMethod(Teardown.class, control.get(), new Class[]{Journey.class}, new Object[]{journey});
    }
}
