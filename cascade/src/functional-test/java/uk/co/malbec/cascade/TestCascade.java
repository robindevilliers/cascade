package uk.co.malbec.cascade;

import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.*;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestCascade {


    @Test
    public void givenOneStep_CascadeShouldGenerateOneDescriptionAndExecuteOneTest(){

        //given
        ClasspathScanner classpathScannerMock = mock(ClasspathScanner.class);
        when(classpathScannerMock.getTypesAnnotatedWith(Step.class)).thenReturn(new HashSet<Class<?>>(){{
            add(EnterUsernameAndPasswordStep.class);
        }});

        when(classpathScannerMock.getSubTypesOf(EnterUsernameAndPasswordStep.class)).thenReturn(new HashSet<Class>(){{
            add(EnterUsernameAndPasswordStep.Successful.class);
        }});

        RunNotifier runNotifierMock = mock(RunNotifier.class);

        //when
        Cascade cascade = new Cascade(classpathScannerMock);
        
        cascade.init(TestBasicMain.class);

        org.junit.runner.Description description = cascade.getDescription();
        assertEquals("Cascade Tests", description.getDisplayName());

        List<org.junit.runner.Description> children = description.getChildren();
        assertEquals(1, children.size());

        org.junit.runner.Description child = children.get(0);
        assertEquals("Enter username and password succesfully(uk.co.malbec.cascade.TestCascade$TestBasicMain)", child.getDisplayName());

        cascade.run(runNotifierMock);

        //then
        verify(classpathScannerMock).initialise("uk.co.mytest.steps");
        verify(classpathScannerMock).getTypesAnnotatedWith(Step.class);
        verify(classpathScannerMock).getSubTypesOf(EnterUsernameAndPasswordStep.class);
        
        verify(runNotifierMock).fireTestStarted(child);
        verify(runNotifierMock).fireTestFinished(child);
        //TODO add assertions for test execution.
        
    }

    @Step
    public interface EnterUsernameAndPasswordStep  {

        @Description("Enter username and password succesfully")
        public class Successful implements EnterUsernameAndPasswordStep {

            @Given
            public void setup() {
            }

            @When
            public void execute() {
            }

            @Then
            public void check(Throwable f) {
            }
        }
    }


    @Scan("uk.co.mytest.steps")
    public static class TestBasicMain {
        
    }
    
}
