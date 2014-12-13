package uk.co.malbec.cascade.modules;


import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.util.List;

public interface TestExecutor {

    public void init(Class<?> controlClass);
    void executeTest(RunNotifier notifier, Description description, List<Object> steps);
}
