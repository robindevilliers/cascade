package com.github.robindevilliers.cascade.modules;


import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.model.Journey;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.util.List;
import java.util.Map;

public interface TestExecutor {

    void init(Class<?> controlClass, Map<String, Scope> globalScope);

    void executeTest(RunNotifier notifier, Description description, List<Object> steps, Journey journey, TestReport reporter, Map<String, Scope> scope);
}
