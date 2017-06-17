package com.github.robindevilliers.cascade;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
import com.github.robindevilliers.cascade.modules.Scanner;
import com.github.robindevilliers.cascade.modules.executor.StandardTestExecutor;
import javassist.compiler.ast.ASTList;
import org.junit.Before;
import org.junit.Test;

import com.github.robindevilliers.cascade.modules.JourneyGenerator;
import com.github.robindevilliers.cascade.modules.completeness.StandardCompletenessStrategy;
import com.github.robindevilliers.cascade.modules.construction.StandardConstructionStrategy;
import com.github.robindevilliers.cascade.modules.filtering.StandardFilterStrategy;
import com.github.robindevilliers.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator;
import com.github.robindevilliers.cascade.modules.reporter.DisableReporter;
import com.github.robindevilliers.cascade.modules.reporter.RenderingSystem;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class TestOneScenario {

    static int count;

    static List<Integer> doThisSetupCalled = new ArrayList<>();
    static List<Integer> doThisExecuteCalled = new ArrayList<>();
    static List<Integer> doThisCheckCalled = new ArrayList<>();
    static List<Integer> doThisClearCalled = new ArrayList<>();

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
        Scanner classpathScannerMock = mock(Scanner.class);
        when(classpathScannerMock.findScenarios(any())).thenReturn(singletonList(new Scenario(Do.DoThis.class, Do.class)));

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

        Description description = cascade.getDescription();
        assertEquals("Cascade Tests", description.getDisplayName());

        List<Description> children = description.getChildren();
        assertEquals(1, children.size());

        Description child = children.get(0);
        assertTrue(child.getDisplayName().matches("Test\\[1\\].*DoThis.*"));

        cascade.run(runNotifierMock);

        //then
        verify(runNotifierMock).fireTestStarted(child);
        verify(runNotifierMock).fireTestFinished(child);

        assertEquals("[1]", doThisSetupCalled.toString());
        assertEquals("[2]", doThisExecuteCalled.toString());
        assertEquals("[3]", doThisCheckCalled.toString());
        assertEquals("[4]", doThisClearCalled.toString());
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
    }


    @Scan("uk.co.mytest.steps")
    public static class TestBasicMain {

    }

}
