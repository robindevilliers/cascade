package uk.co.malbec.cascade;


import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.handler.Handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static uk.co.malbec.cascade.utils.ReflectionUtils.*;

public class StandardTestExecutor implements TestExecutor {

    private Handler[] preHandlers = new Handler[0];
    private Handler[] postHandlers = new Handler[0];

    @Override
    public void init(Class<?> controlClass) {
        StepPreHandler stepPreHandlerAnnotation = controlClass.getAnnotation(StepPreHandler.class);
        if (stepPreHandlerAnnotation != null){
            List<Handler> handlers = new ArrayList<Handler>();
            for (Class cls : stepPreHandlerAnnotation.value()){
                try {
                    handlers.add((Handler) newInstance(cls));
                } catch (IllegalAccessException e) {
                    throw new CascadeException("Illegal access exception trying to instantiate handler class", e);
                } catch (InstantiationException e) {
                    throw new CascadeException("Instantiation exception trying to instantiate handler class", e);
                } catch (ClassCastException e){
                    throw new CascadeException("Class specified as handler class is not an instance of Handler", e);
                }

            }
            preHandlers = handlers.toArray(new Handler[handlers.size()]);
        }

        StepPostHandler stepPostHandlerAnnotation = controlClass.getAnnotation(StepPostHandler.class);
        if (stepPostHandlerAnnotation != null){
            List<Handler> handlers = new ArrayList<Handler>();
            for (Class cls : stepPostHandlerAnnotation.value()){
                try {
                    handlers.add((Handler) newInstance(cls));
                } catch (IllegalAccessException e) {
                    throw new CascadeException("Illegal access exception trying to instantiate handler class", e);
                } catch (InstantiationException e) {
                    throw new CascadeException("Instantiation exception trying to instantiate handler class", e);
                } catch (ClassCastException e){
                    throw new CascadeException("Class specified as handler class is not an instance of Handler", e);
                }
            }
            postHandlers = handlers.toArray(new Handler[handlers.size()]);
        }
    }


    public void executeTest(RunNotifier notifier, Description description, List<Object> steps) {

        //TODO - write tests for pre and post handlers

        notifier.fireTestStarted(description);

        for (Object step : steps) {

            for (Handler handler : preHandlers){
                handler.handle(step);
            }

            StepPreHandler stepPreHandlerAnnotation = step.getClass().getAnnotation(StepPreHandler.class);
            if (stepPreHandlerAnnotation != null){
                for (Class cls : stepPreHandlerAnnotation.value()){
                    try {
                        ((Handler) newInstance(cls)).handle(step);
                    } catch (IllegalAccessException e) {
                        throw new CascadeException("Illegal access exception trying to instantiate handler class", e);
                    } catch (InstantiationException e) {
                        throw new CascadeException("Instantiation exception trying to instantiate handler class", e);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as handler class is not an instance of Handler", e);
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

            Method thenMethod = findAnnotatedMethod(Then.class, step);

            if (thenMethod != null) {
                try {
                    thenMethod.invoke(step, exceptionResult);

                } catch (InvocationTargetException e) {
                    notifier.fireTestFailure(new Failure(description, e.getTargetException()));
                    break;
                } catch (Exception e) {
                    notifier.fireTestFailure(new Failure(description, e));
                    break;
                }
            }

            StepPostHandler stepPostHandlerAnnotation = step.getClass().getAnnotation(StepPostHandler.class);
            if (stepPostHandlerAnnotation != null){
                for (Class cls : stepPostHandlerAnnotation.value()){
                    try {
                        ((Handler) newInstance(cls)).handle(step);
                    } catch (IllegalAccessException e) {
                        throw new CascadeException("Illegal access exception trying to instantiate handler class", e);
                    } catch (InstantiationException e) {
                        throw new CascadeException("Instantiation exception trying to instantiate handler class", e);
                    } catch (ClassCastException e){
                        throw new CascadeException("Class specified as handler class is not an instance of Handler", e);
                    }
                }
            }

            for (Handler handler : postHandlers){
                handler.handle(step);
            }
        }

        notifier.fireTestFinished(description);
    }
}
