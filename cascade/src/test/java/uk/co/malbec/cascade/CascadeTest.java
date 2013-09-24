package uk.co.malbec.cascade;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Scan;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.util.TestUtil;
import uk.co.malbec.cascade.utils.Reference;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CascadeTest {

    Cascade cascade;

    ClasspathScanner classpathScannerMock = mock(ClasspathScanner.class);

    ScenarioFinder scenarioFinderMock = mock(ScenarioFinder.class);

    JourneyGenerator journeyGeneratorMock = mock(JourneyGenerator.class);

    ConstructionStrategy constructionStrategyMock = mock(ConstructionStrategy.class);

    TestExecutor testExecutorMock = mock(TestExecutor.class);

    FilterStrategy filterStrategyMock = mock(FilterStrategy.class);

    RunNotifier runNotifierMock = mock(RunNotifier.class);

    @Before
    public void setup() {
        cascade = new Cascade(classpathScannerMock, scenarioFinderMock, journeyGeneratorMock, constructionStrategyMock, testExecutorMock, filterStrategyMock);
    }

    @Test
    public void initMethodShouldGenerateJourneys() {
        //given
        List<Class> scenariosMock = mock(List.class);
        when(scenarioFinderMock.findScenarios(any(String[].class), any(ClasspathScanner.class))).thenReturn(scenariosMock);

        List<Journey> journeys = mock(List.class);
        when(journeyGeneratorMock.generateJourneys(any(List.class), any(Class.class))).thenReturn(journeys);

        //When
        cascade.init(TestClass.class);

        //then
        verify(filterStrategyMock).init(TestClass.class);
        verify(scenarioFinderMock).findScenarios(new String[]{"uk.co.this", "uk.co.that"}, classpathScannerMock);
        verify(journeyGeneratorMock).generateJourneys(scenariosMock, TestClass.class);
        assertEquals(journeys, TestUtil.getField(cascade, "journeys"));
        assertEquals(TestClass.class, TestUtil.getField(cascade, "controlClass"));
    }

    @Test
    public void getDescriptionMethodShouldGenerateDescriptionsFromJourneys() {
        //given
        List<Journey> journeys = new ArrayList<Journey>() {{
            add(new Journey(new ArrayList<Class>() {{
                add(Her.class);
                add(Him.class);
            }}, TestClass.class));
            add(new Journey(new ArrayList<Class>() {{
                add(Him.class);
                add(Her.class);
            }}, TestClass.class));
        }};

        TestUtil.setField(cascade, "journeys", journeys);

        //when
        Description description = cascade.getDescription();

        //then
        List<Description> children = description.getChildren();
        assertEquals(2, children.size());
        assertEquals(" class uk.co.malbec.cascade.CascadeTest$Her  class uk.co.malbec.cascade.CascadeTest$Him (uk.co.malbec.cascade.CascadeTest$TestClass)", children.get(0).getDisplayName());
        assertEquals(" class uk.co.malbec.cascade.CascadeTest$Him  class uk.co.malbec.cascade.CascadeTest$Her (uk.co.malbec.cascade.CascadeTest$TestClass)", children.get(1).getDisplayName());
    }

    @Test
    public void runMethodShouldExecuteATestForEachJourney() {
        //given
        List<Journey> journeys = new ArrayList<Journey>() {{
            add(new Journey(new ArrayList<Class>() {{
                add(Her.class);
                add(Him.class);
            }}, TestClass.class));
            add(new Journey(new ArrayList<Class>() {{
                add(Him.class);
                add(Her.class);
            }}, TestClass.class));
        }};

        TestUtil.setField(cascade, "journeys", journeys);
        TestUtil.setField(cascade, "controlClass", TestClass.class);

        when(filterStrategyMock.match(journeys.get(0))).thenReturn(true);
        when(filterStrategyMock.match(journeys.get(1))).thenReturn(true);

        //when
        cascade.run(runNotifierMock);

        //then
        verify(filterStrategyMock).match(journeys.get(0));
        verify(constructionStrategyMock).setup(eq(TestClass.class), eq(journeys.get(0)), any(Reference.class), any(Reference.class));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys.get(0).getDescription(), null);

        verify(filterStrategyMock).match(journeys.get(1));
        verify(constructionStrategyMock).setup(eq(TestClass.class), eq(journeys.get(1)), any(Reference.class), any(Reference.class));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys.get(1).getDescription(), null);

        verify(constructionStrategyMock, times(2)).tearDown(any(Reference.class), any(Reference.class));
    }

    @Test
    public void runMethodShouldNotExecuteTestsThatAreFilteredOut() {
        //given
        List<Journey> journeys = new ArrayList<Journey>() {{
            add(new Journey(new ArrayList<Class>() {{
                add(Her.class);
                add(Him.class);
            }}, TestClass.class));
            add(new Journey(new ArrayList<Class>() {{
                add(Him.class);
                add(Her.class);
            }}, TestClass.class));
        }};

        TestUtil.setField(cascade, "journeys", journeys);
        TestUtil.setField(cascade, "controlClass", TestClass.class);

        when(filterStrategyMock.match(journeys.get(0))).thenReturn(true);
        when(filterStrategyMock.match(journeys.get(1))).thenReturn(false);

        //when
        cascade.run(runNotifierMock);

        //then
        verify(filterStrategyMock).match(journeys.get(0));
        verify(constructionStrategyMock).setup(eq(TestClass.class), eq(journeys.get(0)), any(Reference.class), any(Reference.class));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys.get(0).getDescription(), null);
        verify(constructionStrategyMock).tearDown(any(Reference.class), any(Reference.class));
    }

    @Step
    public static class Her {

    }

    @Step
    public static class Him {

    }

    @Scan({"uk.co.this", "uk.co.that"})
    public static class TestClass {

    }

}
