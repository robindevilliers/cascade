package uk.co.malbec.cascade;


import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static uk.co.malbec.cascade.utils.ReflectionUtils.*;

public class StandardTestExecutor implements TestExecutor {

    public void executeTest(RunNotifier notifier, Description description, List<Object> steps) {

        notifier.fireTestStarted(description);

        for (Object step : steps) {

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
        }

        notifier.fireTestFinished(description);
    }
}
