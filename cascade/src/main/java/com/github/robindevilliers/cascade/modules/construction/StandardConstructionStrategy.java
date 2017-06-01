package com.github.robindevilliers.cascade.modules.construction;


import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.annotations.Clear;
import com.github.robindevilliers.cascade.annotations.Given;
import com.github.robindevilliers.cascade.annotations.Setup;
import com.github.robindevilliers.cascade.annotations.Teardown;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.modules.ConstructionStrategy;
import com.github.robindevilliers.cascade.utils.Reference;
import com.github.robindevilliers.cascade.utils.ReflectionUtils;

import java.util.*;

public class StandardConstructionStrategy implements ConstructionStrategy {

    @Override
    public Map<String, Scope> setup(Class<?> controlClass, Journey journey, Reference<Object> control, Reference<List<Object>> steps, Map<String, Scope> globalScope) {
        Map<String, Scope> scope = new HashMap<>(globalScope);

        control.set(ReflectionUtils.newInstance(controlClass, "control"));

        steps.set(new ArrayList<>());
        Map<Scenario, Object> singletons = new HashMap<Scenario, Object>();
        for (Scenario scenario : journey.getSteps()) {
            singletons.computeIfAbsent(scenario, k -> ReflectionUtils.newInstance(scenario.getClazz(), "step"));
            steps.get().add(singletons.get(scenario));
        }

        ReflectionUtils.collectSuppliedFields(control.get(), scope);

        for (Object step : steps.get()) {
            ReflectionUtils.collectSuppliedFields(step, scope);
        }

        for (Object step : steps.get()) {
            ReflectionUtils.injectDemandedFields(step, scope);
        }

        Set<Object> alreadyInitialised = new HashSet<Object>();
        for (Object step : steps.get()) {
            if (!alreadyInitialised.contains(step)) {
                ReflectionUtils.invokeAnnotatedMethod(Given.class, step);
                alreadyInitialised.add(step);
            }
        }

        for (Object step : steps.get()) {
            ReflectionUtils.collectSuppliedFields(step, scope);
        }

        ReflectionUtils.injectDemandedFields(control.get(), scope);

        ReflectionUtils.invokeAnnotatedMethod(Setup.class, control.get(), new Class[]{Journey.class}, new Object[]{journey});

        ReflectionUtils.collectSuppliedFields(control.get(), scope);

        for (Object step : steps.get()) {
            ReflectionUtils.injectDemandedFields(step, scope);
        }

        return scope;
    }

    @Override
    public void tearDown(Reference<Object> control, Journey journey, Reference<List<Object>> steps) {
        for (Object step : steps.get()) {
            ReflectionUtils.invokeAnnotatedMethod(Clear.class, step);
        }
        ReflectionUtils.invokeAnnotatedMethod(Teardown.class, control.get(), new Class[]{Journey.class}, new Object[]{journey});
    }
}
