package uk.co.malbec.cascade.modules.generator


import org.junit.Before
import org.junit.Test
import uk.co.malbec.cascade.Scenario
import uk.co.malbec.cascade.annotations.OnlyRunWith
import uk.co.malbec.cascade.annotations.ReEntrantTerminator
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.annotations.Terminator
import uk.co.malbec.cascade.conditions.ConditionalLogic
import uk.co.malbec.cascade.conditions.Predicate
import uk.co.malbec.cascade.exception.CascadeException
import uk.co.malbec.cascade.model.Journey
import uk.co.malbec.cascade.modules.FilterStrategy

import static org.mockito.Mockito.mock
import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

import static uk.co.malbec.cascade.conditions.Predicates.withStep

class StepBackwardsFromTerminatorsJourneyGeneratorTest {

    StepBackwardsFromTerminatorsJourneyGenerator backwardsFromTerminatorsJourneyGenerator;

    FilterStrategy filterStrategyMock = mock(FilterStrategy);

    @Before
    def void "initialisation"() {
        backwardsFromTerminatorsJourneyGenerator = new StepBackwardsFromTerminatorsJourneyGenerator(new ConditionalLogic(),1)
    }

    @Test
    def void "given a set of dependent steps, the journey generator should generate journeys"() {
        when(filterStrategyMock.match(any(Journey))).thenReturn(true)

        //when
        List<Journey> journeys = backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                new Scenario(OpenLoginPage),
                new Scenario(BadPassword),
                new Scenario(Successful),
                new Scenario(PostLoginAlert.UserAccountLockedAlert),
                new Scenario(PostLoginAlert.InformationAlert),
                new Scenario(DisplayAccountsList.MortgageAccount),
                new Scenario(DisplayAccountsList.SaverAccount),
                new Scenario(DisplaySaverAccount)
        ], TestClass, filterStrategyMock)

        //then
        Journey journey;

        //we should have the simplest most happy journey for mortgages
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.MortgageAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by the account list page showing mortgage"
        journeys.remove(journey)

        //we should have the simplest most happy journey for saver accounts
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.SaverAccount) &&
                    steps.remove(0) == new Scenario(DisplaySaverAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by the account list page showing saver account and then the saver account page"
        journeys.remove(journey)

        //we should have the journey that includes an optional informational message that does not terminate
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.InformationAlert) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.MortgageAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by an informational message and then the mortgage list page"
        journeys.remove(journey)

        // we should have the journey that has the account locked alert which is a terminating journey
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.UserAccountLockedAlert) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge and a message stating that the account is locked"
        journeys.remove(journey)

        //we should have all the above tests preceded by an incorrect password entry
        //simplest journey first
        journey = journeys.find { it ->
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.SaverAccount) &&
                    steps.remove(0) == new Scenario(DisplaySaverAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success and then the saver list page and then the saver details."
        journeys.remove(journey)

        //informational message with bad password
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.InformationAlert) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.MortgageAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success, an information message and then the mortgage list page."
        journeys.remove(journey)

        //informational message with bad password
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.InformationAlert) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.SaverAccount) &&
                    steps.remove(0) == new Scenario(DisplaySaverAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success, an information message and then the saver list page and then the saver details page."
        journeys.remove(journey)

        //user account locked with bad password
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword) &&
                    steps.remove(0) == new Scenario(Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.UserAccountLockedAlert) &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success and a message stating that the account is locked"
        journeys.remove(journey)

        //if this is empty then we don't have any unexpected journeys.
        // notably, we don't have saver details scenarioClasses following mortgage list which is prohibited by OnRunWith tag.
        assert journeys.empty
    }

    @Step
    static class OpenLoginPage {
    }

    @Step([OpenLoginPage, BadPassword])
    static class Successful {
    }

    @Step([OpenLoginPage])
    static class BadPassword {
    }

    @Step([Successful])
    static interface PostLoginAlert {

        @Terminator
        static class UserAccountLockedAlert implements PostLoginAlert {
        }

        static class InformationAlert implements PostLoginAlert {
        }
    }

    @Step([Successful, PostLoginAlert.InformationAlert])
    static interface DisplayAccountsList {
        static class MortgageAccount implements DisplayAccountsList {
        }

        static class SaverAccount implements DisplayAccountsList {
        }
    }

    @Step([DisplayAccountsList])
    static class DisplaySaverAccount {
        @OnlyRunWith
        Predicate predicate = withStep(DisplayAccountsList.SaverAccount)
    }


    @Test
    def void "given a set of re-entrant steps, the journey generator should generate journeys"() {
        //re-entrant scenarioClasses are scenarioClasses that can occur multiple times in a journey, but will

        when(filterStrategyMock.match(any(Journey))).thenReturn(true)

        //when
        List<Journey> journeys = backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                new Scenario(OpenLandingPage),
                new Scenario(OpenHomePage),
                new Scenario(OpenDetailsPage)
        ], TestClass, filterStrategyMock)

        //then
        Journey journey;

        //journey with the OpenDetailsPage repeated twice.
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.size() == 5 &&
                    steps.remove(0) == new Scenario(OpenLandingPage) &&
                    steps.remove(0) == new Scenario(OpenHomePage) &&
                    steps.remove(0) == new Scenario(OpenDetailsPage) &&
                    steps.remove(0) == new Scenario(OpenHomePage) &&
                    steps.remove(0) == new Scenario(OpenDetailsPage) &&
                    steps.empty
        }
        assert journey: "expected a journey with the open details page occurring exactly twice and the journey should end on the open details page"
        journeys.remove(journey)

    }

    @Test
    def void "given a set of re-entrant steps with no re-entrant-terminators, the journey generator should throw an exception"() {
        //re-entrant scenarioClasses are scenarioClasses that can occur multiple times in a journey, but will
        when(filterStrategyMock.match(any(Journey))).thenReturn(true)

        try {
            //when
            backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                    new Scenario(OpenHomePage),
                    new Scenario(OpenDetailsPageThatDoesntTerminate),
                    new Scenario(OpenDetailsPage)
            ], TestClass, filterStrategyMock)

            assert false: "An infinite loop exists, so an exception is expected"
        } catch (CascadeException e) {
            //then
            assert e.message == 'Invalid configuration.  An infinite loop is configured. [StepBackwardsFromTerminatorsJourneyGeneratorTest$OpenHomePage StepBackwardsFromTerminatorsJourneyGeneratorTest$OpenDetailsPageThatDoesntTerminate StepBackwardsFromTerminatorsJourneyGeneratorTest$OpenHomePage]'
        }
    }

    @Step
    static class OpenLandingPage {
    }

    @Step([OpenLandingPage, OpenDetailsPage, OpenDetailsPageThatDoesntTerminate])
    static class OpenHomePage {
    }

    @Step(OpenHomePage)
    static class OpenDetailsPageThatDoesntTerminate {
    }

    @Step(OpenHomePage)
    @ReEntrantTerminator(2)
    static class OpenDetailsPage {
    }

    @Test
    def void "given a set of self referring steps, the generator should throw an exception"() {
        //re-entrant scenarioClasses are scenarioClasses that can occur multiple times in a journey, but will
        when(filterStrategyMock.match(any(Journey))).thenReturn(true)

        try {
            //when
            backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                    new Scenario(A),
                    new Scenario(B)
            ], TestClass, filterStrategyMock)

            assert false: "An infinite loop exists, so an exception is expected"
        } catch (CascadeException e) {
            //then
            assert e.message == 'Invalid configuration: Scenario class uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGeneratorTest$A not found in any journey: This journey generator calculates journeys by finding terminators and walking backwards to the steps that start journeys. If a step is not found in journeys, it is either dependent on steps that don\'t lead to a journey start, or there are no terminators downstream of this step.'
        }
    }

    @Test
    def void "given a set of non-termnating steps, with no downstream terminators, the generator should throw an exception"() {
        //re-entrant scenarioClasses are scenarioClasses that can occur multiple times in a journey, but will
        when(filterStrategyMock.match(any(Journey))).thenReturn(true)

        try {
            //when
            backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                    new Scenario(A),
                    new Scenario(B),
                    new Scenario(C)
            ], TestClass, filterStrategyMock)

            assert false: "An infinite loop exists, so an exception is expected"
        } catch (CascadeException e) {
            //then
            assert e.message == 'Invalid configuration: Scenario class uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGeneratorTest$A not found in any journey: This journey generator calculates journeys by finding terminators and walking backwards to the steps that start journeys. If a step is not found in journeys, it is either dependent on steps that don\'t lead to a journey start, or there are no terminators downstream of this step.'
        }
    }

    @Step(B)
    static class A {
    }

    @Step([A, C])
    static class B {
    }

    @Step
    static class C {
    }

    public static class TestClass {
    }
}
