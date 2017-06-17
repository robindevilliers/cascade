package com.github.robindevilliers.cascade;


import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.cascade.modules.JourneyGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
import com.github.robindevilliers.cascade.modules.Scanner;
import com.github.robindevilliers.cascade.modules.completeness.StandardCompletenessStrategy;
import com.github.robindevilliers.cascade.modules.construction.StandardConstructionStrategy;
import com.github.robindevilliers.cascade.modules.executor.StandardTestExecutor;
import com.github.robindevilliers.cascade.modules.filtering.StandardFilterStrategy;
import com.github.robindevilliers.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator;
import com.github.robindevilliers.cascade.modules.reporter.DisableReporter;
import com.github.robindevilliers.cascade.modules.reporter.RenderingSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestTwoScenarios {

    static int count;

    static List<Integer> doThisSetupCalled = new ArrayList<>();
    static List<Integer> doThisExecuteCalled = new ArrayList<>();
    static List<Integer> doThisCheckCalled = new ArrayList<>();
    static List<Integer> doThisClearCalled = new ArrayList<>();

    static List<Integer> doThatSetupCalled = new ArrayList<>();
    static List<Integer> doThatExecuteCalled = new ArrayList<>();
    static List<Integer> doThatCheckCalled = new ArrayList<>();
    static List<Integer> doThatClearCalled = new ArrayList<>();

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
        Scanner classpathScannerMock = mock(Scanner.class);
        when(classpathScannerMock.getTypesAnnotatedWith(Step.class)).thenReturn(new HashSet<Class<?>>() {{
            add(Do.class);
        }});

        when(classpathScannerMock.getSubTypesOf(Do.class)).thenReturn(new HashSet<Class>() {{
            add(Do.DoThis.class);
            add(Do.DoThat.class);
        }});

        RunNotifier runNotifierMock = mock(RunNotifier.class);

        JourneyGenerator journeyGenerator = new StepBackwardsFromTerminatorsJourneyGenerator();

        journeyGenerator.init(new ConditionalLogic());

        //when
        Cascade cascade = new Cascade(classpathScannerMock,
                new ScenarioFinder(),
                journeyGenerator,
                new StandardConstructionStrategy(),
                new StandardTestExecutor(),
                new StandardFilterStrategy(new ConditionalLogic()),
                new StandardCompletenessStrategy(),
                new DisableReporter(),
                new RenderingSystem());

        cascade.init(TestBasicMain.class);

        Description description = cascade.getDescription();
        assertEquals("Cascade Tests", description.getDisplayName());

        List<Description> children = description.getChildren();
        assertEquals(2, children.size());

        Description child0 = children.get(0);
        assertTrue(child0.getDisplayName().matches("Test\\[1\\].*DoThat.*"));

        Description child1 = children.get(1);
        assertTrue(child1.getDisplayName().matches("Test\\[2\\].*DoThis.*"));

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

        @Narrative("Do This")
        class DoThis implements Do {

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
            public void check() {
                count++;
                doThisCheckCalled.add(count);
            }

            @Clear
            public void cleanup() {
                count++;
                doThisClearCalled.add(count);
            }
        }

        @Narrative("Do That")
        class DoThat implements Do {

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
            public void check() {
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
