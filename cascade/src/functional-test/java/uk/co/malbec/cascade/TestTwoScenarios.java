package uk.co.malbec.cascade;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.modules.ClasspathScanner;
import uk.co.malbec.cascade.modules.construction.StandardConstructionStrategy;
import uk.co.malbec.cascade.modules.executor.StandardTestExecutor;
import uk.co.malbec.cascade.modules.filtering.StandardFilterStrategy;
import uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Ignore
public class TestTwoScenarios {

    static int count;

    static List<Integer> doThisSetupCalled = new ArrayList<Integer>();
    static List<Integer> doThisExecuteCalled = new ArrayList<Integer>();
    static List<Integer> doThisCheckCalled = new ArrayList<Integer>();
    static List<Integer> doThisClearCalled = new ArrayList<Integer>();

    static List<Integer> doThatSetupCalled = new ArrayList<Integer>();
    static List<Integer> doThatExecuteCalled = new ArrayList<Integer>();
    static List<Integer> doThatCheckCalled = new ArrayList<Integer>();
    static List<Integer> doThatClearCalled = new ArrayList<Integer>();


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
    }

    @Test
    public void givenOneStep_CascadeShouldGenerateOneDescriptionAndExecuteOneTest() {

        //given
        ClasspathScanner classpathScannerMock = mock(ClasspathScanner.class);
        when(classpathScannerMock.getTypesAnnotatedWith(Step.class)).thenReturn(new HashSet<Class<?>>() {{
            add(Do.class);
        }});

        when(classpathScannerMock.getSubTypesOf(Do.class)).thenReturn(new HashSet<Class>() {{
            add(Do.DoThis.class);
            add(Do.DoThat.class);
        }});

        RunNotifier runNotifierMock = mock(RunNotifier.class);

        //when
        Cascade<Step, Page> cascade = new Cascade<>(classpathScannerMock,
                new EdgeFinder<>(Step.class),
                new VertexFinder<>(Page.class),
                new StepBackwardsFromTerminatorsJourneyGenerator(new ConditionalLogic(),1),
                new StandardConstructionStrategy(),
                new StandardTestExecutor(),
                new StandardFilterStrategy(new ConditionalLogic()));

        cascade.init(TestBasicMain.class);

        org.junit.runner.Description description = cascade.getDescription();
        assertEquals("Cascade Tests", description.getDisplayName());

        List<org.junit.runner.Description> children = description.getChildren();
        assertEquals(2, children.size());

        org.junit.runner.Description child0 = children.get(0);
        assertEquals("Test[1] Do That(uk.co.malbec.cascade.TestTwoScenarios$TestBasicMain)", child0.getDisplayName());

        org.junit.runner.Description child1 = children.get(1);
        assertEquals("Test[2] Do This(uk.co.malbec.cascade.TestTwoScenarios$TestBasicMain)", child1.getDisplayName());

        cascade.run(runNotifierMock);

        //then
        verify(classpathScannerMock).initialise("uk.co.mytest.steps");
        verify(classpathScannerMock).getTypesAnnotatedWith(Step.class);
        verify(classpathScannerMock).getSubTypesOf(Do.class);

        verify(runNotifierMock).fireTestStarted(child0);
        verify(runNotifierMock).fireTestFinished(child0);

        assertEquals("[1]", doThatSetupCalled.toString());
        assertEquals("[2]", doThatExecuteCalled.toString());
        assertEquals("[3]", doThatCheckCalled.toString());
        assertEquals("[4]", doThatClearCalled.toString());

        assertEquals("[5]", doThisSetupCalled.toString());
        assertEquals("[6]", doThisExecuteCalled.toString());
        assertEquals("[7]", doThisCheckCalled.toString());
        assertEquals("[8]", doThisClearCalled.toString());
    }


    @Step
    public interface Do {

        @Description("Do This")
        public class DoThis implements Do {

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

        @Description("Do That")
        public class DoThat implements Do {

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
    }


    @Scan("uk.co.mytest.steps")
    public static class TestBasicMain {

    }
}
