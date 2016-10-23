package uk.co.malbec.cascade;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
public class TestThreeScenariosWithTerminatorOverTwoSteps {

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
            add(DoOneStep.class);
            add(DoTwo.class);
        }});

        when(classpathScannerMock.getSubTypesOf(DoOneStep.class)).thenReturn(new HashSet<Class>() {{
            add(DoOneStep.DoThisStep.class);
            add(DoOneStep.DoTheOtherStep.class);
        }});

        when(classpathScannerMock.getSubTypesOf(DoTwo.class)).thenReturn(new HashSet<Class>() {{
            add(DoTwo.DoThat.class);
        }});

        RunNotifier runNotifierMock = mock(RunNotifier.class);

        //when
        Cascade<Step, Page> cascade = new Cascade<>(classpathScannerMock,
                new EdgeFinder<>(Step.class),
                new VertexFinder<>(Page.class),
                new StepBackwardsFromTerminatorsJourneyGenerator(new ConditionalLogic(), 1),
                new StandardConstructionStrategy(),
                new StandardTestExecutor(),
                new StandardFilterStrategy(new ConditionalLogic()));

        cascade.init(TestBasicMain.class);

        org.junit.runner.Description description = cascade.getDescription();
        assertEquals("Cascade Tests", description.getDisplayName());

        List<Description> children = description.getChildren();
        assertEquals(2, children.size());

        org.junit.runner.Description child0 = children.get(0);
        assertTrue(child0.getDisplayName().startsWith("Test[1] Do The Other"));

        org.junit.runner.Description child1 = children.get(1);
        assertTrue(child1.getDisplayName().startsWith("Test[2] Do ThisDo That"));

        cascade.run(runNotifierMock);

        //then
        verify(classpathScannerMock).initialise("uk.co.mytest.steps");
        verify(classpathScannerMock).getTypesAnnotatedWith(Step.class);
        verify(classpathScannerMock).getSubTypesOf(DoOneStep.class);
        verify(classpathScannerMock).getSubTypesOf(DoTwo.class);

        verify(runNotifierMock).fireTestStarted(child0);
        verify(runNotifierMock).fireTestFinished(child0);

        assertEquals("[1]", doTheOtherSetupCalled.toString());
        assertEquals("[2]", doTheOtherExecuteCalled.toString());
        assertEquals("[3]", doTheOtherCheckCalled.toString());
        assertEquals("[4]", doTheOtherClearCalled.toString());

        assertEquals("[5]", doThisSetupCalled.toString());
        assertEquals("[7]", doThisExecuteCalled.toString());
        assertEquals("[8]", doThisCheckCalled.toString());
        assertEquals("[11]", doThisClearCalled.toString());

        assertEquals("[6]", doThatSetupCalled.toString());
        assertEquals("[9]", doThatExecuteCalled.toString());
        assertEquals("[10]", doThatCheckCalled.toString());
        assertEquals("[12]", doThatClearCalled.toString());
    }


    @Step
    public interface DoOneStep {

        @uk.co.malbec.cascade.annotations.Description("Do This")
        public class DoThisStep implements DoOneStep {

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



            @Clear
            public void cleanup() {
                count++;
                doThisClearCalled.add(count);
            }
        }

        @uk.co.malbec.cascade.annotations.Description("Do The Other")
        @Terminator
        public class DoTheOtherStep implements DoOneStep {

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



            @Clear
            public void cleanup() {
                count++;
                doTheOtherClearCalled.add(count);
            }
        }
    }

    @Page
    public interface DoOnePage {

        public class OnePageVersionOne implements DoOnePage {
            @Then
            public void check(Throwable f) {
                count++;
                doThisCheckCalled.add(count);
            }
        }

        public class OnePageVersionTwo implements DoOnePage {
            @Then
            public void check(Throwable f) {
                count++;
                doTheOtherCheckCalled.add(count);
            }
        }
    }

    @Step(DoOneStep.class)
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
    }


    @Scan("uk.co.mytest.steps")
    public static class TestBasicMain {

    }
}
