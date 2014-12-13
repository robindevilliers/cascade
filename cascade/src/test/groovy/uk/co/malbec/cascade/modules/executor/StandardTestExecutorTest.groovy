package uk.co.malbec.cascade.modules.executor

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.events.Handler
import uk.co.malbec.cascade.modules.TestExecutor
import uk.co.malbec.cascade.modules.executor.StandardTestExecutor;

import static org.mockito.Mockito.*;

public class StandardTestExecutorTest {


    static Integer counter
    static Integer whenCalled
    static Integer thenCalled
    static Integer preHandlerCalled
    static Integer handlerCalled
    static Integer postHandlerCalled
    static Integer preHandlerCtrlCalled
    static Integer handlerCtrlCalled
    static Integer postHandlerCtrlCalled

    static Throwable thrownException
    static Throwable receivedException


    @Before
    public void before(){
        counter = 0;
        whenCalled = null;
        thenCalled = null;
        preHandlerCalled = null;
        handlerCalled = null;
        postHandlerCalled = null;
        preHandlerCtrlCalled = null;
        handlerCtrlCalled = null;
        postHandlerCtrlCalled = null;
    }

    @Test
    public void basicTest() {

        TestExecutor testExecutor = new StandardTestExecutor();

        testExecutor.init(ControlClass);

        RunNotifier runNotifier = mock(RunNotifier);

        Description description = mock(Description);

        testExecutor.executeTest(runNotifier, description,[new BasicStep()]);

        assert preHandlerCtrlCalled == 0
        assert preHandlerCalled == 1
        assert whenCalled == 2
        assert handlerCalled == 3
        assert handlerCtrlCalled == 4
        assert thenCalled == 5
        assert postHandlerCalled == 6
        assert postHandlerCtrlCalled == 7

        verify(runNotifier).fireTestStarted(description)
        verify(runNotifier).fireTestFinished(description)
    }

    @Test
    public void executeWithException(){
        TestExecutor testExecutor = new StandardTestExecutor();

        testExecutor.init(ControlClass);

        RunNotifier runNotifier = mock(RunNotifier);

        Description description = mock(Description);

        thrownException = mock(Throwable)

        testExecutor.executeTest(runNotifier, description,[new ExceptionStep()]);

        assert thrownException == receivedException
    }

    @Step
    @StepPreHandler(PreHandler)
    @StepHandler(Normalhandler)
    @StepPostHandler(PostHandler)
    static class BasicStep {

        @When
        public void when(){

            whenCalled = counter++;
        }

        @Then
        public void then(Throwable f){
            thenCalled = counter++;

        }
    }

    @Step
    static class ExceptionStep {

        @When
        public void when(){
            whenCalled = counter++;
            throw thrownException
        }

        @Then
        public void then(Throwable f){
            receivedException = f

        }
    }

    public static class PreHandlerCtrl implements Handler {

        @Override
        void handle(Object step) {
            preHandlerCtrlCalled = counter++;
        }
    }

    public static class HandlerCtrl implements Handler {

        @Override
        void handle(Object step) {
            handlerCtrlCalled = counter++;
        }
    }

    public static class PostHandlerCtrl implements Handler {

        @Override
        void handle(Object step) {
            postHandlerCtrlCalled = counter++;
        }
    }

    public static class PreHandler implements Handler {

        @Override
        public void handle(Object step) {
            preHandlerCalled = counter++;
        }
    }

    public static class Normalhandler implements Handler {

        @Override
        void handle(Object step) {
            handlerCalled = counter++;
        }
    }

    public static class PostHandler implements Handler {

        @Override
        public void handle(Object step) {
            postHandlerCalled = counter++;
        }
    }


    @StepPreHandler(PreHandlerCtrl)
    @StepHandler(HandlerCtrl)
    @StepPostHandler(PostHandlerCtrl)
    public static class ControlClass {
    }
}
