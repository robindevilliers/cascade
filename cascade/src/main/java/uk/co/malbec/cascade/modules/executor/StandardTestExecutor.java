package uk.co.malbec.cascade.modules.executor;


import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.Edge;
import uk.co.malbec.cascade.Thig;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.events.Handler;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.TestExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static uk.co.malbec.cascade.utils.ReflectionUtils.findAnnotatedMethod;
import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class StandardTestExecutor implements TestExecutor {

    private Handler[] preHandlers = new Handler[0];
    private Handler[] handlers = new Handler[0];
    private Handler[] postHandlers = new Handler[0];

    @Override
    public void init(Class<?> controlClass) {
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


    public void executeTest(RunNotifier notifier, Description description, List<Object> steps, Journey journey) {

        //TODO - write tests for pre and post handlers
        notifier.fireTestStarted(description);

        System.out.println(journey.getName().replaceAll("\\s+", "\n\t"));
        System.out.println("----------------- Filter ----------------------------------");
        System.out.println("@FilterTests");
        System.out.println("Predicate filter = and(");

        boolean comma = false;
        int index = 0;
        for (Thig edge : journey.getTrail()){
            if (comma){
                System.out.println(",");
            }
            System.out.print("\tstepAt(");
            System.out.print(index++);
            System.out.print(",");
            System.out.print(edge.getCls().getCanonicalName());
            System.out.print(".class)");
            comma = true;
        }

        System.out.println("\n);");
        System.out.println("----------------- Results ---------------------------------");

        boolean testSuccess = true;
        for (Object step : steps) {

            for (Handler handler : preHandlers){
                handler.handle(step);
            }

            StepPreHandler stepPreHandlerAnnotation = step.getClass().getAnnotation(StepPreHandler.class);
            if (stepPreHandlerAnnotation != null){
                for (Class cls : stepPreHandlerAnnotation.value()){
                    try {
                        ((Handler) newInstance(cls, "step pre events")).handle(step);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                    }
                }
            }

            Method whenMethod = findAnnotatedMethod(When.class, step);
            Throwable exceptionResult = null;
            if (whenMethod != null) {
                try {
                    whenMethod.invoke(step);
                } catch (InvocationTargetException e) {
                    exceptionResult = e.getTargetException();
                } catch (Exception e) {

                    //TODO - come up with a better way to handle this.
                    e.printStackTrace();
                }
            }

            //TODO - consider getting rid of step handlers - if steps have either When or then and not both, then this is redundant
            StepHandler stepHandlerAnnotation = step.getClass().getAnnotation(StepHandler.class);
            if (stepHandlerAnnotation != null){
                for (Class cls : stepHandlerAnnotation.value()){
                    try {
                        ((Handler) newInstance(cls, "step events")).handle(step);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                    }
                }
            }

            for (Handler handler : handlers){
                handler.handle(step);
            }

            Method thenMethod = findAnnotatedMethod(Then.class, step);

            if (thenMethod != null) {
                try {
                    thenMethod.invoke(step, exceptionResult);

                } catch (InvocationTargetException e) {
                    notifier.fireTestFailure(new Failure(description, e.getTargetException()));
                    testSuccess = false;
                    break;
                } catch (Exception e) {
                    notifier.fireTestFailure(new Failure(description, e));
                    testSuccess = false;
                    break;
                }
            }

            StepPostHandler stepPostHandlerAnnotation = step.getClass().getAnnotation(StepPostHandler.class);
            if (stepPostHandlerAnnotation != null){
                for (Class cls : stepPostHandlerAnnotation.value()){
                    try {
                        ((Handler) newInstance(cls, "step post events")).handle(step);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as events class is not an instance of Handler", e);
                    }
                }
            }

            for (Handler handler : postHandlers){
                handler.handle(step);
            }
        }

        System.out.println(testSuccess ? "SUCCESS" : "FAILURE");

        System.out.println("-----------------------------------------------------------");

        notifier.fireTestFinished(description);
    }
}
