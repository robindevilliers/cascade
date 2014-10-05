package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.Clear;
import uk.co.malbec.cascade.annotations.Given;
import uk.co.malbec.cascade.annotations.Setup;
import uk.co.malbec.cascade.annotations.Teardown;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.utils.Reference;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.co.malbec.cascade.utils.ReflectionUtils.*;

public class StandardConstructionStrategy implements ConstructionStrategy {

    @Override
    public void setup(Class<?> controlClass, Journey journey, Reference<Object> control, Reference<List<Object>> steps) {
        Map<String, Object> scope = new HashMap<String, Object>();

        try {
            control.set(newInstance(controlClass));
        } catch (IllegalAccessException e) {
            throw new CascadeException("Illegal access exception trying to instantiate control class", e);
        } catch (InstantiationException e) {
            throw new CascadeException("Instantiation exception trying to instantiate control class", e);
        }

        steps.set(new ArrayList<Object>());
        for (Class clazz : journey.getSteps()) {
            try {
                steps.get().add(newInstance(clazz));
            } catch (IllegalAccessException e) {
                throw new CascadeException(String.format("Illegal access exception trying to instantiate step class: %s", clazz.toString()), e);
            } catch (InstantiationException e) {
                throw new CascadeException(String.format("Instantiation exception trying to instantiate step class: %s", clazz.toString()), e);
            }
        }


        collectSuppliedFields(control.get(), scope);

        for (Object step : steps.get()) {
            collectSuppliedFields(step, scope);
        }

        for (Object step : steps.get()) {
            injectDemandedFields(step, scope);
        }

        for (Object step : steps.get()) {
            try {
                invokeAnnotatedMethod(Given.class, step);
            } catch (InvocationTargetException e) {
                throw new CascadeException(String.format("step class through exception executing Given method: %s", step.getClass().toString()), e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new CascadeException(String.format("Illegal access exception trying to execute Given method on step class: %s", step.getClass().toString()), e);
            }
        }

        for (Object step : steps.get()) {
            collectSuppliedFields(step, scope);
        }

        injectDemandedFields(control.get(), scope);

        try {
            invokeAnnotatedMethod(Setup.class, control.get());
        } catch (InvocationTargetException e) {
            throw new CascadeException("control class through exception executing Setup method", e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new CascadeException("Illegal access exception trying to execute Setup method on control class", e);
        }

        collectSuppliedFields(control.get(), scope);

        for (Object step : steps.get()) {
            injectDemandedFields(step, scope);
        }
    }

    @Override
    public void tearDown(Reference<Object> control, Reference<List<Object>> steps) {

        for (Object step : steps.get()) {
            try {
                invokeAnnotatedMethod(Clear.class, step);
            } catch (InvocationTargetException e) {
                throw new CascadeException(String.format("step class through exception executing Clear method: %s", step.getClass().toString()), e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new CascadeException(String.format("Illegal access exception trying to execute Clear method on step class: %s", step.getClass().toString()), e);
            }
        }

        try {
            invokeAnnotatedMethod(Teardown.class, control.get());
        } catch (InvocationTargetException e) {
            throw new CascadeException("control class through exception executing Teardown method", e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new CascadeException("Illegal access exception trying to execute Teardown method on control class", e);
        }
    }
}
