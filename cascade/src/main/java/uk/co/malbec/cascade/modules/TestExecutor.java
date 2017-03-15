package uk.co.malbec.cascade.modules;


import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.model.Journey;

import java.util.List;
import java.util.Map;

public interface TestExecutor {
    void init(Class<?> controlClass, Map<String, Scope> globalScope);

    void executeTest(RunNotifier notifier, Description description, List<Object> steps, Journey journey, Reporter reporter, Map<String, Scope> scope);
}
