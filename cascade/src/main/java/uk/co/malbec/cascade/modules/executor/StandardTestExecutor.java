package uk.co.malbec.cascade.modules.executor;


import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.events.Handler;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.Reporter;
import uk.co.malbec.cascade.modules.TestExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.co.malbec.cascade.utils.ReflectionUtils.*;

public class StandardTestExecutor implements TestExecutor {

    private Handler[] preHandlers = new Handler[0];
    private Handler[] handlers = new Handler[0];
    private Handler[] postHandlers = new Handler[0];

    @Override
    public void init(Class<?> controlClass, Map<String, Scope> globalScope) {
        StepPreHandler stepPreHandlerAnnotation = controlClass.getAnnotation(StepPreHandler.class);
        if (stepPreHandlerAnnotation != null){
            List<Handler> handlers = new ArrayList<Handler>();
            for (Class cls : stepPreHandlerAnnotation.value()){
                try {
                    handlers.add((Handler) newInstance(cls, "step pre events"));
                } catch (ClassCastException e){
                    throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                }
            }
            preHandlers = handlers.toArray(new Handler[handlers.size()]);
        }

        StepHandler stepHandlerAnnotation = controlClass.getAnnotation(StepHandler.class);
        if (stepHandlerAnnotation != null){
            List<Handler> handlers = new ArrayList<Handler>();
            for (Class cls : stepHandlerAnnotation.value()){
                try {
                    handlers.add((Handler) newInstance(cls, "step events"));
                } catch (ClassCastException e){
                    throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                }
            }
            this.handlers = handlers.toArray(new Handler[handlers.size()]);
        }

        StepPostHandler stepPostHandlerAnnotation = controlClass.getAnnotation(StepPostHandler.class);
        if (stepPostHandlerAnnotation != null){
            List<Handler> handlers = new ArrayList<Handler>();
            for (Class cls : stepPostHandlerAnnotation.value()){
                try {
                    handlers.add((Handler) newInstance(cls, "step post events"));
                } catch (ClassCastException e){
                    throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                }
            }
            postHandlers = handlers.toArray(new Handler[handlers.size()]);
        }
    }


    public void executeTest(RunNotifier notifier, Description description, List<Object> steps, Journey journey, Reporter reporter, Map<String, Scope> scope) {

        //TODO - consider collecting supplied fields again.


        notifier.fireTestStarted(description);

        boolean testPassed = true;
        for (Object step : steps) {

            reporter.stepBegin(step);

            for (Handler handler : preHandlers){
                injectDemandedFields(handler, scope);
                handler.handle(step);
                collectSuppliedFields(handler, scope);
            }

            StepPreHandler stepPreHandlerAnnotation = step.getClass().getAnnotation(StepPreHandler.class);
            if (stepPreHandlerAnnotation != null){
                for (Class cls : stepPreHandlerAnnotation.value()){
                    try {
                        Handler handler = ((Handler) newInstance(cls, "step pre events"));
                        injectDemandedFields(handler, scope);
                        handler.handle(step);
                        collectSuppliedFields(handler, scope);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                    }
                }
            }

            Method whenMethod = findAnnotatedMethod(When.class, step);
            if (whenMethod != null) {
                reporter.stepWhenBegin(step, whenMethod);
                try {
                    whenMethod.invoke(step);
                    reporter.setWhenSuccess(step);
                } catch (InvocationTargetException e) {
                    notifier.fireTestFailure(new Failure(description, e.getTargetException()));
                    reporter.stepWhenInvocationException(step, whenMethod, e);
                    testPassed = false;
                    break;
                } catch (Exception e) {
                    throw new CascadeException("Unknown exception occurred while trying to execute When method of step: " + step.getClass(), e);
                }
                reporter.stepWhenEnd(step, whenMethod);
            }

            for (Handler handler : handlers){
                injectDemandedFields(handler, scope);
                handler.handle(step);
                collectSuppliedFields(handler, scope);
            }

            StepHandler stepHandlerAnnotation = step.getClass().getAnnotation(StepHandler.class);
            if (stepHandlerAnnotation != null){
                for (Class cls : stepHandlerAnnotation.value()){
                    try {
                        Handler handler = ((Handler) newInstance(cls, "step events"));
                        injectDemandedFields(handler, scope);
                        handler.handle(step);
                        collectSuppliedFields(handler, scope);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                    }
                }
            }

            Method thenMethod = findAnnotatedMethod(Then.class, step);

            if (thenMethod != null) {
                reporter.stepThenBegin(step, thenMethod);
                try {
                    thenMethod.invoke(step);
                    reporter.stepThenSuccess(step);
                } catch (InvocationTargetException e) {
                    notifier.fireTestFailure(new Failure(description, e.getTargetException()));
                    reporter.stepThenInvocationException(step, thenMethod, e);
                    testPassed = false;
                    break;
                } catch (Exception e) {
                    throw new CascadeException("Unknown exception occurred while trying to execute Then method of step: " + step.getClass(), e);
                }
                reporter.stepThenEnd(step, thenMethod);
            }

            StepPostHandler stepPostHandlerAnnotation = step.getClass().getAnnotation(StepPostHandler.class);
            if (stepPostHandlerAnnotation != null){
                for (Class cls : stepPostHandlerAnnotation.value()){
                    try {
                        Handler handler = ((Handler) newInstance(cls, "step post events"));
                        injectDemandedFields(handler, scope);
                        handler.handle(step);
                        collectSuppliedFields(handler, scope);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                    }
                }
            }

            for (Handler handler : postHandlers){
                injectDemandedFields(handler, scope);
                handler.handle(step);
                collectSuppliedFields(handler, scope);
            }

            reporter.endStep(step);
        }
        notifier.fireTestFinished(description);
        if (testPassed) {
            reporter.success(journey);
        }
    }
}
