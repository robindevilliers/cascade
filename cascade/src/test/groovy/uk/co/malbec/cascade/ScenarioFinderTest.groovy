package uk.co.malbec.cascade

import org.junit.After
import org.junit.Before
import org.junit.Test
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.modules.ClasspathScanner

import static org.mockito.Mockito.*

class ScenarioFinderTest {

    ScenarioFinder scenarioFinder

    ClasspathScanner classpathScannerMock = mock(ClasspathScanner);

    @Before
    public void "initialisation"() {
        scenarioFinder = new ScenarioFinder()
    }

    @After
    public void "cleanup"() {
        verifyNoMoreInteractions(classpathScannerMock)
    }

    @Test
    def void "given a request to find scenarios, the scenario finder should search in classes found by the classpath scanner for scenarios"() {

        //given
        String[] paths = ['org.this', 'org.that']

        when(classpathScannerMock.getTypesAnnotatedWith(Step)).thenReturn(
                [Him, Her] as Set<Class<?>>, //returned for first call
                [] as Set<Class<?>> //returned for second call
        )

        //when
        List<Class> scenarios = scenarioFinder.findScenarios(paths, classpathScannerMock)

        //then
        verify(classpathScannerMock).initialise(paths[0])
        verify(classpathScannerMock, times(2)).getTypesAnnotatedWith(Step)
        verify(classpathScannerMock).initialise(paths[1])

        assert scenarios.size() == 2
        scenarios[0] == Him
        scenarios[1] == Her
    }

    @Test
    def void "given step interface format, the scenario finder should find classes composed within interfaces"(){
        //given
        String[] paths = ['org.this']

        when(classpathScannerMock.getTypesAnnotatedWith(Step)).thenReturn(
                [LoginStep] as Set<Class<?>>
        )
        when(classpathScannerMock.getSubTypesOf(LoginStep)).thenReturn([
                LoginStep.Successful,
                LoginStep.TooManyTimes,
                LoginStep.BadPassword,
        ]  as Set<Class>)

        //when
        List<Class> scenarios = scenarioFinder.findScenarios(paths, classpathScannerMock)

        //then
        verify(classpathScannerMock).initialise(paths[0])
        verify(classpathScannerMock).getTypesAnnotatedWith(Step)
        verify(classpathScannerMock).getSubTypesOf(LoginStep)

        assert scenarios.size() == 3
        scenarios[0] == LoginStep.Successful
        scenarios[1] == LoginStep.TooManyTimes
        scenarios[2] == LoginStep.BadPassword
    }

    @Step
    public static class Her {
    }

    @Step
    public static class Him {
    }

    @Step
    public interface LoginStep {
        public class Successful implements LoginStep {
        }

        public class TooManyTimes implements LoginStep {
        }

        public class BadPassword implements LoginStep {
        }
    }
}
