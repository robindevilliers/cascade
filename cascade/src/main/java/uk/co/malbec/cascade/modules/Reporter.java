package uk.co.malbec.cascade.modules;

import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.utils.Reference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface Reporter {

    void init(Class<?> controlClass, List<Scenario> scenarios, Map<String, Scope> globalScope);

    void startTest(Journey journey, Reference<Object> control, Reference<List<Object>> steps);

    void tearDown(Reference<Object> control, Reference<List<Object>> steps);

    void stepBegin(Object step);

    void stepWhenBegin(Object step, Method whenMethod);

    void stepWhenInvocationException(Object step, Method whenMethod, InvocationTargetException e);

    void stepWhenEnd(Object step, Method whenMethod);

    void setWhenSuccess(Object step);

    void stepThenBegin(Object step, Method thenMethod);

    void stepThenSuccess(Object step);

    void stepThenInvocationException(Object step, Method thenMethod, InvocationTargetException e);

    void stepThenEnd(Object step, Method thenMethod);

    void handleUnknownException(RuntimeException e, Journey journey);

    void endStep(Object step);

    void finishTest(Journey journey);

    void start();

    void finish();

    void setupTest(Journey journey, Map<String, Scope> scope);

    void success(Journey journey);
}
