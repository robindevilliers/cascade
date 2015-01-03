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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestOneScenario {

    static int count;

    static List<Integer> doThisSetupCalled = new ArrayList<Integer>();
    static List<Integer> doThisExecuteCalled = new ArrayList<Integer>();
    static List<Integer> doThisCheckCalled = new ArrayList<Integer>();
    static List<Integer> doThisClearCalled = new ArrayList<Integer>();

    @Before
    public void setup() {
        count = 0;
        doThisSetupCalled.clear();
        doThisExecuteCalled.clear();
        doThisCheckCalled.clear();
        doThisClearCalled.clear();
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
        }});

        RunNotifier runNotifierMock = mock(RunNotifier.class);

        //when
        Cascade cascade = new Cascade(classpathScannerMock,
                new ScenarioFinder(),
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
        assertEquals("Test[1] Do This(uk.co.malbec.cascade.TestOneScenario$TestBasicMain)", child.getDisplayName());

        cascade.run(runNotifierMock);

        //then
        verify(classpathScannerMock).initialise("uk.co.mytest.steps");
        verify(classpathScannerMock).getTypesAnnotatedWith(Step.class);
        verify(classpathScannerMock).getSubTypesOf(Do.class);

        verify(runNotifierMock).fireTestStarted(child);
        verify(runNotifierMock).fireTestFinished(child);

        assertEquals("[1]", doThisSetupCalled.toString());
        assertEquals("[2]", doThisExecuteCalled.toString());
        assertEquals("[3]", doThisCheckCalled.toString());
        assertEquals("[4]", doThisClearCalled.toString());
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
    }


    @Scan("uk.co.mytest.steps")
    public static class TestBasicMain {

    }

}
