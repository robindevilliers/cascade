package uk.co.malbec.cascade;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestThreeScenariosOverTwoSteps {

    static int count;
    static List<Integer> doThisSetupCalled = new ArrayList<Integer>();
    static List<Integer> doThisExecuteCalled = new ArrayList<Integer>();
    static List<Integer> doThisCheckCalled = new ArrayList<Integer>();
    static List<Integer> doThisClearCalled = new ArrayList<Integer>();

    static List<Integer> doThatSetupCalled = new ArrayList<Integer>();
    static List<Integer> doThatExecuteCalled = new ArrayList<Integer>();
    static List<Integer> doThatCheckCalled = new ArrayList<Integer>();
    static List<Integer> doThatClearCalled = new ArrayList<Integer>();

    static List<Integer> doTheOtherSetupCalled = new ArrayList<Integer>();
    static List<Integer> doTheOtherExecuteCalled = new ArrayList<Integer>();
    static List<Integer> doTheOtherCheckCalled = new ArrayList<Integer>();
    static List<Integer> doTheOtherClearCalled = new ArrayList<Integer>();


    @Before
    public void setup() {
        count = 0;

        doThisSetupCalled.clear();
        doThisExecuteCalled.clear();
        doThisCheckCalled.clear();
        doThisClearCalled.clear();

        doThatSetupCalled.clear();
        doThatExecuteCalled.clear();
        doThatCheckCalled.clear();
        doThatClearCalled.clear();

        doTheOtherSetupCalled.clear();
        doTheOtherExecuteCalled.clear();
        doTheOtherCheckCalled.clear();
        doTheOtherClearCalled.clear();
    }

    @Test
    public void givenOneStep_CascadeShouldGenerateOneDescriptionAndExecuteOneTest() {

        //given
        ClasspathScanner classpathScannerMock = mock(ClasspathScanner.class);
        when(classpathScannerMock.getTypesAnnotatedWith(Step.class)).thenReturn(new HashSet<Class<?>>() {{
            add(DoOne.class);
            add(DoTwo.class);
        }});

        when(classpathScannerMock.getSubTypesOf(DoOne.class)).thenReturn(new HashSet<Class>() {{
            add(DoOne.DoThis.class);

        }});

        when(classpathScannerMock.getSubTypesOf(DoTwo.class)).thenReturn(new HashSet<Class>() {{
            add(DoTwo.DoThat.class);
            add(DoTwo.DoTheOther.class);
        }});

        RunNotifier runNotifierMock = mock(RunNotifier.class);

        //when
        Cascade cascade = new Cascade(classpathScannerMock, new ScenarioFinder(), new StepBackwardsFromTerminatorsJourneyGenerator(), new StandardConstructionStrategy(), new StandardTestExecutor());

        cascade.init(TestBasicMain.class);

        org.junit.runner.Description description = cascade.getDescription();
        assertEquals("Cascade Tests", description.getDisplayName());

        List<Description> children = description.getChildren();
        assertEquals(2, children.size());

        org.junit.runner.Description child0 = children.get(0);
        assertTrue(child0.getDisplayName().startsWith("Do ThisDo That"));

        org.junit.runner.Description child1 = children.get(1);
        assertTrue(child1.getDisplayName().startsWith("Do ThisDo The Other"));

        cascade.run(runNotifierMock);

        //then
        verify(classpathScannerMock).initialise("uk.co.mytest.steps");
        verify(classpathScannerMock).getTypesAnnotatedWith(Step.class);
        verify(classpathScannerMock).getSubTypesOf(DoOne.class);
        verify(classpathScannerMock).getSubTypesOf(DoTwo.class);

        verify(runNotifierMock).fireTestStarted(child0);
        verify(runNotifierMock).fireTestFinished(child0);

        assertEquals("[1, 9]", doThisSetupCalled.toString());
        assertEquals("[3, 11]", doThisExecuteCalled.toString());
        assertEquals("[4, 12]", doThisCheckCalled.toString());
        assertEquals("[7, 15]", doThisClearCalled.toString());

        assertEquals("[2]", doThatSetupCalled.toString());
        assertEquals("[5]", doThatExecuteCalled.toString());
        assertEquals("[6]", doThatCheckCalled.toString());
        assertEquals("[8]", doThatClearCalled.toString());

        assertEquals("[10]", doTheOtherSetupCalled.toString());
        assertEquals("[13]", doTheOtherExecuteCalled.toString());
        assertEquals("[14]", doTheOtherCheckCalled.toString());
        assertEquals("[16]", doTheOtherClearCalled.toString());
    }


    @Step
    public interface DoOne {

        @uk.co.malbec.cascade.annotations.Description("Do This")
        public class DoThis implements DoOne {

            @Given
            public void setup() {
                count++;

                doThisSetupCalled.add(count);
            }

            @When
            public void execute() {
                count++;
                doThisExecuteCalled.add(count);
            }

            @Then
            public void check(Throwable f) {
                count++;
                doThisCheckCalled.add(count);
            }

            @Clear
            public void cleanup() {
                count++;
                doThisClearCalled.add(count);
            }
        }
    }

    @Step(DoOne.class)
    public interface DoTwo {


        @uk.co.malbec.cascade.annotations.Description("Do That")
        public class DoThat implements DoTwo {

            @Given
            public void setup() {
                count++;
                doThatSetupCalled.add(count);
            }

            @When
            public void execute() {
                count++;
                doThatExecuteCalled.add(count);
            }

            @Then
            public void check(Throwable f) {
                count++;
                doThatCheckCalled.add(count);
            }

            @Clear
            public void cleanup() {
                count++;
                doThatClearCalled.add(count);
            }
        }

        @uk.co.malbec.cascade.annotations.Description("Do The Other")
        public class DoTheOther implements DoTwo {

            @Given
            public void setup() {
                count++;
                doTheOtherSetupCalled.add(count);
            }

            @When
            public void execute() {
                count++;
                doTheOtherExecuteCalled.add(count);
            }

            @Then
            public void check(Throwable f) {
                count++;
                doTheOtherCheckCalled.add(count);
            }

            @Clear
            public void cleanup() {
                count++;
                doTheOtherClearCalled.add(count);
            }
        }
    }


    @Scan("uk.co.mytest.steps")
    public static class TestBasicMain {

    }
}
