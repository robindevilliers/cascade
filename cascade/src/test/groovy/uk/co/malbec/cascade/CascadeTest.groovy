package uk.co.malbec.cascade

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import uk.co.malbec.cascade.annotations.Scan
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.model.Journey
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
        when(journeyGeneratorMock.generateJourneys(any(List), any(Class))).thenReturn(journeysMock)

        //When
        cascade.init(TestClass);

        //then
        verify(filterStrategyMock).init(TestClass);
        verify(testExecutorMock).init(TestClass);
        verify(scenarioFinderMock).findScenarios(["uk.co.this", "uk.co.that"] as String[], classpathScannerMock)
        verify(journeyGeneratorMock).generateJourneys(scenariosMock, TestClass)

        assert journeysMock == cascade.journeys
        assert TestClass == cascade.controlClass
    }

    @Test
    public void "given a generated cascade test suit, a call to getDescription should generate a description for each journey"() {
        //given
        cascade.journeys = [new Journey([Her, Him], TestClass), new Journey([Him, Her], TestClass)]
        for (Journey journey : cascade.journeys){
            journey.init()
        }

        //when
        Description description = cascade.getDescription()

        //then
        assert description.displayName == 'Cascade Tests'
        List<Description> children = description.getChildren()
        assert children.size() == 2
        assert children[0].displayName == ' CascadeTest$Her  CascadeTest$Him (uk.co.malbec.cascade.CascadeTest$TestClass)'
        assert children[1].displayName == ' CascadeTest$Him  CascadeTest$Her (uk.co.malbec.cascade.CascadeTest$TestClass)'
    }

    @Test
    public void "given a request to run, the cascade class should filter, setup, execute and then teardown all journeys "() {
        //given
        List<Journey> journeys = [new Journey([Her, Him], TestClass), new Journey([Him, Her], TestClass)]
        cascade.journeys = journeys
        for (Journey journey : cascade.journeys){
            journey.init()
        }
        cascade.controlClass = TestClass
       
        when(filterStrategyMock.match(journeys[0])).thenReturn(true);
        when(filterStrategyMock.match(journeys[1])).thenReturn(true);

        //when
        cascade.run(runNotifierMock);

        //then
        verify(filterStrategyMock).match(journeys[0]);
        verify(constructionStrategyMock).setup(eq(TestClass), eq(journeys[0]), any(Reference), any(Reference));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys[0].getDescription(), null);

        verify(filterStrategyMock).match(journeys[1]);
        verify(constructionStrategyMock).setup(eq(TestClass), eq(journeys[1]), any(Reference), any(Reference));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys[1].getDescription(), null);

        verify(constructionStrategyMock, times(2)).tearDown(any(Reference), any(Reference));
    }

    @Test
    public void "given a request to run, the cascade class should filter out scenarios"() {
        //given
        List<Journey> journeys = [new Journey([Her, Him], TestClass), new Journey([Him, Her], TestClass)]
        cascade.journeys = journeys
        cascade.controlClass = TestClass

        when(filterStrategyMock.match(journeys[0])).thenReturn(true);
        when(filterStrategyMock.match(journeys[1])).thenReturn(false);

        //when
        cascade.run(runNotifierMock);

        //then
        verify(filterStrategyMock).match(journeys[0]);
        verify(constructionStrategyMock).setup(eq(TestClass), eq(journeys[0]), any(Reference), any(Reference));
        verify(testExecutorMock).executeTest(runNotifierMock, journeys[0].getDescription(), null);
        verify(constructionStrategyMock).tearDown(any(Reference), any(Reference));

        verify(filterStrategyMock).match(journeys[1]);
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
