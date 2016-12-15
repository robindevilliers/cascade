package uk.co.malbec.cascade.modules;


import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.model.Journey;

import java.util.List;

public interface TestExecutor {
    void init(Class<?> controlClass);
    void executeTest(RunNotifier notifier, Description description, List<Object> steps, Journey journey);
}
