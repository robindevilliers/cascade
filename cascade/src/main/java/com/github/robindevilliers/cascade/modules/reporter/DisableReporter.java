package com.github.robindevilliers.cascade.modules.reporter;

import com.github.robindevilliers.cascade.Completeness;
import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.utils.Reference;
import com.github.robindevilliers.cascade.modules.Reporter;
import com.github.robindevilliers.cascade.modules.TestReport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DisableReporter implements Reporter {

    @Override
    public void init(Class<?> controlClass, List<Scenario> scenarios, Map<String, Scope> globalScope, Completeness completenessLevel, RenderingSystem renderingSystem) {

    }

    @Override
    public void start() {

    }

    @Override
    public TestReport createTestReport() {
        return new DisableTestReport();
    }

    @Override
    public void finish() {

    }


    public static class DisableTestReport implements TestReport {

        @Override
        public void setupTest(Journey journey, Map<String, Scope> scope) {

        }

        @Override
        public void startTest(Journey journey, Reference<Object> control, Reference<List<Object>> steps) {

        }

        @Override
        public void stepBegin(Object step) {

        }

        @Override
        public void stepWhenBegin(Object step, Method whenMethod) {

        }

        @Override
        public void stepWhenInvocationException(Object step, Method whenMethod, InvocationTargetException e) {

        }

        @Override
        public void stepWhenEnd(Object step, Method whenMethod) {

        }

        @Override
        public void setWhenSuccess(Object step) {

        }

        @Override
        public void stepThenBegin(Object step, Method thenMethod) {

        }

        @Override
        public void stepThenSuccess(Object step) {

        }

        @Override
        public void stepThenInvocationException(Object step, Method thenMethod, InvocationTargetException e) {

        }

        @Override
        public void stepThenEnd(Object step, Method thenMethod) {

        }

        @Override
        public void endStep(Object step) {

        }

        @Override
        public void tearDown(Reference<Object> control, Reference<List<Object>> steps) {

        }

        @Override
        public void handleUnknownException(RuntimeException e, Journey journey) {

        }

        @Override
        public void finishTest(Journey journey) {

        }

        @Override
        public void success(Journey journey) {

        }

        @Override
        public void mergeTestReport() {

        }
    }
}
