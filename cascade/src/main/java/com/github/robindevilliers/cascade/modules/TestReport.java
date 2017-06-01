package com.github.robindevilliers.cascade.modules;

import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.utils.Reference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface TestReport {

    void setupTest(Journey journey, Map<String, Scope> scope);

    void startTest(Journey journey, Reference<Object> control, Reference<List<Object>> steps);

    void stepBegin(Object step);

    void stepWhenBegin(Object step, Method whenMethod);

    void stepWhenInvocationException(Object step, Method whenMethod, InvocationTargetException e);

    void stepWhenEnd(Object step, Method whenMethod);

    void setWhenSuccess(Object step);

    void stepThenBegin(Object step, Method thenMethod);

    void stepThenSuccess(Object step);

    void stepThenInvocationException(Object step, Method thenMethod, InvocationTargetException e);

    void stepThenEnd(Object step, Method thenMethod);

    void endStep(Object step);

    void tearDown(Reference<Object> control, Reference<List<Object>> steps);

    void handleUnknownException(RuntimeException e, Journey journey);

    void finishTest(Journey journey);

    void success(Journey journey);

    void mergeTestReport();
}
