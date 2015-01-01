package uk.co.malbec.cascade

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import uk.co.malbec.cascade.annotations.Scan
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.model.Journey
import uk.co.malbec.cascade.modules.ClasspathScanner
import uk.co.malbec.cascade.modules.ConstructionStrategy
import uk.co.malbec.cascade.modules.FilterStrategy
import uk.co.malbec.cascade.modules.JourneyGenerator
import uk.co.malbec.cascade.modules.TestExecutor
import uk.co.malbec.cascade.utils.Reference
import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.*

class CascadeTest {

    Cascade cascade;

    ClasspathScanner classpathScannerMock = mock(ClasspathScanner);

    ScenarioFinder scenarioFinderMock = mock(ScenarioFinder);

    JourneyGenerator journeyGeneratorMock = mock(JourneyGenerator);

    ConstructionStrategy constructionStrategyMock = mock(ConstructionStrategy);

    TestExecutor testExecutorMock = mock(TestExecutor);

    FilterStrategy filterStrategyMock = mock(FilterStrategy);

    RunNotifier runNotifierMock = mock(RunNotifier);

    @Before
    public void "initialisation"() {
        cascade = new Cascade(classpathScannerMock, scenarioFinderMock, journeyGeneratorMock, constructionStrategyMock, testExecutorMock, filterStrategyMock);
    }

    @After
    public void "cleanup"(){
        verifyNoMoreInteractions(classpathScannerMock, scenarioFinderMock, journeyGeneratorMock, constructionStrategyMock, testExecutorMock, filterStrategyMock)
    }

    @Test
    public void "given start of test, the cascade class should find scenarios and delegate to journey generator to generate journeys"() {
        //given
        List<Class> scenariosMock = mock(List)
        when(scenarioFinderMock.findScenarios(any(String[]), any(ClasspathScanner))).thenReturn(scenariosMock)

        List<Journey> journeysMock = mock(List)
        when(journeyGeneratorMock.generateJourneys(any(List), any(Class), eq(filterStrategyMock))).thenReturn(journeysMock)

        //When
        cascade.init(TestClass);

        //then
        verify(filterStrategyMock).init(TestClass);
        verify(testExecutorMock).init(TestClass);
        verify(scenarioFinderMock).findScenarios(["uk.co.this", "uk.co.that"] as String[], classpathScannerMock)
        verify(journeyGeneratorMock).generateJourneys(scenariosMock, TestClass, filterStrategyMock)

        assert journeysMock == cascade.journeys
        assert TestClass == cascade.controlClass
    }

    @Test
    public void "given a generated cascade test suit, a call to getDescription should generate a description for each journey"() {
        //given
        cascade.journeys = [new Journey([Her, Him], TestClass), new Journey([Him, Her], TestClass)]

        int i = 1;
        for (Journey journey : cascade.journeys){
            journey.init(i++)
        }

        //when
        Description description = cascade.getDescription()

        //then
        assert description.displayName == 'Cascade Tests'
        List<Description> children = description.getChildren()
        assert children.size() == 2
        assert children[0].displayName == 'Test[1]  CascadeTest$Her  CascadeTest$Him (uk.co.malbec.cascade.CascadeTest$TestClass)'
        assert children[1].displayName == 'Test[2]  CascadeTest$Him  CascadeTest$Her (uk.co.malbec.cascade.CascadeTest$TestClass)'
    }

    @Test
    public void "given a request to run, the cascade class should setup, execute and then teardown all journeys "() {
        //given
        List<Journey> journeys = [new Journey([Her, Him], TestClass), new Journey([Him, Her], TestClass)]
        cascade.journeys = journeys
        int i = 1;
        for (Journey journey : cascade.journeys){
            journey.init(i++)
        }
        cascade.controlClass = TestClass

        //when
        cascade.run(runNotifierMock);

        //then
        verify(constructionStrategyMock).setup(eq(TestClass), eq(journeys[0]), any(Reference), any(Reference));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys[0].getDescription(), null, journeys[0]);

        verify(constructionStrategyMock).setup(eq(TestClass), eq(journeys[1]), any(Reference), any(Reference));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys[1].getDescription(), null, journeys[1]);

        verify(constructionStrategyMock, times(2)).tearDown(any(Reference), any(Reference));
    }

    @Step
    public static class Her {

    }

    @Step
    public static class Him {

    }

    @Scan(["uk.co.this", "uk.co.that"])
    public static class TestClass {

    }
}
