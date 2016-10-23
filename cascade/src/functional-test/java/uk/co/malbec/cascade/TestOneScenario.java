package uk.co.malbec.cascade;

import org.junit.Before;
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class TestOneScenario {

    static int operationCount;

    static List<Integer> doThisGivenCalled = new ArrayList<Integer>();
    static List<Integer> doThisWhenCalled = new ArrayList<Integer>();
    static List<Integer> doThisClearCalled = new ArrayList<Integer>();

    static List<Integer> beHereGivenCalled = new ArrayList<Integer>();
    static List<Integer> beHereCheckCalled = new ArrayList<Integer>();
    static List<Integer> beHereClearCalled = new ArrayList<Integer>();

    @Before
    public void setup() {
        operationCount = 0;
        doThisGivenCalled.clear();
        doThisWhenCalled.clear();
        doThisClearCalled.clear();

        beHereGivenCalled.clear();
        beHereCheckCalled.clear();
        beHereClearCalled.clear();
    }

    @Test
    public void givenOneStep_CascadeShouldGenerateOneDescriptionAndExecuteOneTest() {

        //given
        ClasspathScanner classpathScannerMock = mock(ClasspathScanner.class);
        when(classpathScannerMock.getTypesAnnotatedWith(Step.class)).thenReturn(new HashSet<Class<?>>() {{
            add(Do.class);
        }});

        when(classpathScannerMock.getTypesAnnotatedWith(Page.class)).thenReturn(new HashSet<Class<?>>() {{
            add(Be.class);
        }});

        when(classpathScannerMock.getSubTypesOf(Do.class)).thenReturn(new HashSet<Class>() {{
            add(Do.DoThis.class);
        }});

        when(classpathScannerMock.getSubTypesOf(Be.class)).thenReturn(new HashSet<Class>() {{
            add(Be.BeHere.class);
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

        List<org.junit.runner.Description> children = description.getChildren();
        assertEquals(1, children.size());

        org.junit.runner.Description child = children.get(0);
        assertEquals("Test[1] Do ThisBe Here(uk.co.malbec.cascade.TestOneScenario$TestBasicMain)", child.getDisplayName());

        cascade.run(runNotifierMock);

        //then
        verify(classpathScannerMock, times(2)).initialise("uk.co.mytest.steps");
        verify(classpathScannerMock).getTypesAnnotatedWith(Step.class);
        verify(classpathScannerMock).getSubTypesOf(Do.class);

        verify(runNotifierMock).fireTestStarted(child);
        verify(runNotifierMock).fireTestFinished(child);

        assertEquals("[1]", doThisGivenCalled.toString());
        assertEquals("[2]", beHereGivenCalled.toString());
        assertEquals("[3]", doThisWhenCalled.toString());
        assertEquals("[4]", beHereCheckCalled.toString());
        assertEquals("[5]", doThisClearCalled.toString());
        assertEquals("[6]", beHereClearCalled.toString());
    }

    @Step
    public interface Do {

        @Description("Do This")
        public class DoThis implements Do {

            @Given
            public void setup() {
                operationCount++;
                doThisGivenCalled.add(operationCount);
            }

            @When
            public void execute() {
                operationCount++;
                doThisWhenCalled.add(operationCount);
            }

            @Clear
            public void cleanup() {
                operationCount++;
                doThisClearCalled.add(operationCount);
            }
        }
    }

    @Page(Do.class)
    public interface Be {

        @Description("Be Here")
        public class BeHere implements Be {

            @Given
            public void setup() {
                operationCount++;
                beHereGivenCalled.add(operationCount);
            }

            @Then
            public void check(Throwable f) {
                operationCount++;
                beHereCheckCalled.add(operationCount);
            }

            @Clear
            public void cleanup() {
                operationCount++;
                beHereClearCalled.add(operationCount);
            }
        }
    }

    @Scan("uk.co.mytest.steps")
    public static class TestBasicMain {
    }
}
