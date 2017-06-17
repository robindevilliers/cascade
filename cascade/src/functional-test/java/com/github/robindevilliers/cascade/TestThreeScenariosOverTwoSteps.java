package com.github.robindevilliers.cascade;


import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.cascade.modules.JourneyGenerator;
import com.github.robindevilliers.cascade.modules.Scanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestThreeScenariosOverTwoSteps {

    static int count;
    static List<Integer> doThisSetupCalled = new ArrayList<>();
    static List<Integer> doThisExecuteCalled = new ArrayList<>();
    static List<Integer> doThisCheckCalled = new ArrayList<>();
    static List<Integer> doThisClearCalled = new ArrayList<>();

    static List<Integer> doThatSetupCalled = new ArrayList<>();
    static List<Integer> doThatExecuteCalled = new ArrayList<>();
    static List<Integer> doThatCheckCalled = new ArrayList<>();
    static List<Integer> doThatClearCalled = new ArrayList<>();

    static List<Integer> doTheOtherSetupCalled = new ArrayList<>();
    static List<Integer> doTheOtherExecuteCalled = new ArrayList<>();
    static List<Integer> doTheOtherCheckCalled = new ArrayList<>();
    static List<Integer> doTheOtherClearCalled = new ArrayList<>();


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
        Scanner classpathScannerMock = mock(Scanner.class);
        when(classpathScannerMock.findScenarios(any()))
                .thenReturn(asList(
                        new Scenario(DoOne.DoThis.class, DoOne.class),
                        new Scenario(DoTwo.DoThat.class, DoTwo.class),
                        new Scenario(DoTwo.DoTheOther.class, DoTwo.class)
                ));

        RunNotifier runNotifierMock = mock(RunNotifier.class);

        JourneyGenerator journeyGenerator = new StepBackwardsFromTerminatorsJourneyGenerator();

        journeyGenerator.init(new ConditionalLogic());

        //when
        Cascade cascade = new Cascade(classpathScannerMock,
                journeyGenerator,
                new StandardConstructionStrategy(),
                new StandardTestExecutor(),
                new StandardFilterStrategy(new ConditionalLogic()),
                new StandardCompletenessStrategy(),
                new DisableReporter(),
                new RenderingSystem());

        cascade.init(TestBasicMain.class);

        org.junit.runner.Description description = cascade.getDescription();
        assertEquals("Cascade Tests", description.getDisplayName());

        List<Description> children = description.getChildren();
        assertEquals(2, children.size());

        Description child0 = children.get(0);
        assertTrue(child0.getDisplayName().matches("Test\\[1\\].*DoThis.*DoThat.*"));

        Description child1 = children.get(1);
        assertTrue(child1.getDisplayName().matches("Test\\[2\\].*DoThis.*DoTheOther.*"));

        cascade.run(runNotifierMock);

        //then

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

        @Narrative("Do This")
        class DoThis implements DoOne {

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
    }

    @Step(DoOne.class)
    public interface DoTwo {


        @Narrative("Do That")
        class DoThat implements DoTwo {

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

        @Narrative("Do The Other")
        class DoTheOther implements DoTwo {

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
            public void check() {
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
