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

import static org.mockito.Matchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static uk.co.malbec.cascade.conditions.Predicates.withStep

class StepBackwardsFromTerminatorsJourneyGeneratorTest {

    StepBackwardsFromTerminatorsJourneyGenerator backwardsFromTerminatorsJourneyGenerator;

    FilterStrategy filterStrategyMock = mock(FilterStrategy);

    @Before
    def void "initialisation"() {
        backwardsFromTerminatorsJourneyGenerator = new StepBackwardsFromTerminatorsJourneyGenerator()
        backwardsFromTerminatorsJourneyGenerator.init(new ConditionalLogic())
    }

    @Test
    def void "given a set of dependent steps, the journey generator should generate journeys"() {
        when(filterStrategyMock.match(any(Journey))).thenReturn(true)

        //when
        List<Journey> journeys = backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                new Scenario(OpenLoginPage.class, OpenLoginPage),
                new Scenario(BadPassword.class, BadPassword),
                new Scenario(Successful.class, Successful),
                new Scenario(PostLoginAlert.UserAccountLockedAlert.class, PostLoginAlert.UserAccountLockedAlert),
                new Scenario(PostLoginAlert.InformationAlert.class, PostLoginAlert.InformationAlert),
                new Scenario(DisplayAccountsList.MortgageAccount.class, DisplayAccountsList.MortgageAccount),
                new Scenario(DisplayAccountsList.SaverAccount.class, DisplayAccountsList.SaverAccount),
                new Scenario(DisplaySaverAccount.class, DisplaySaverAccount)
        ], TestClass, filterStrategyMock, [:])

        //then
        Journey journey;

        //we should have the simplest most happy journey for mortgages
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.MortgageAccount.class, DisplayAccountsList.MortgageAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by the account list page showing mortgage"
        journeys.remove(journey)

        //we should have the simplest most happy journey for saver accounts
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.SaverAccount.class, DisplayAccountsList.SaverAccount) &&
                    steps.remove(0) == new Scenario(DisplaySaverAccount.class, DisplaySaverAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by the account list page showing saver account and then the saver account page"
        journeys.remove(journey)

        //we should have the journey that includes an optional informational message that does not terminate
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.InformationAlert.class, PostLoginAlert.InformationAlert) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.MortgageAccount.class, DisplayAccountsList.MortgageAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by an informational message and then the mortgage list page"
        journeys.remove(journey)

        // we should have the journey that has the account locked alert which is a terminating journey
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.UserAccountLockedAlert.class, PostLoginAlert.UserAccountLockedAlert) &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge and a message stating that the account is locked"
        journeys.remove(journey)

        //we should have all the above tests preceded by an incorrect password entry
        //simplest journey first
        journey = journeys.find { it ->
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword.class, BadPassword) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.SaverAccount.class, DisplayAccountsList.SaverAccount) &&
                    steps.remove(0) == new Scenario(DisplaySaverAccount.class, DisplaySaverAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success and then the saver list page and then the saver details."
        journeys.remove(journey)

        //informational message with bad password
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword.class, BadPassword) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.InformationAlert.class, PostLoginAlert.InformationAlert) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.MortgageAccount.class, DisplayAccountsList.MortgageAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success, an information message and then the mortgage list page."
        journeys.remove(journey)

        //informational message with bad password
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword.class, BadPassword) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.InformationAlert.class, PostLoginAlert.InformationAlert) &&
                    steps.remove(0) == new Scenario(DisplayAccountsList.SaverAccount.class, DisplayAccountsList.SaverAccount) &&
                    steps.remove(0) == new Scenario(DisplaySaverAccount.class, DisplaySaverAccount) &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success, an information message and then the saver list page and then the saver details page."
        journeys.remove(journey)

        //user account locked with bad password
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.remove(0) == new Scenario(OpenLoginPage.class, OpenLoginPage) &&
                    steps.remove(0) == new Scenario(BadPassword.class, BadPassword) &&
                    steps.remove(0) == new Scenario(Successful.class, Successful) &&
                    steps.remove(0) == new Scenario(PostLoginAlert.UserAccountLockedAlert.class, PostLoginAlert.UserAccountLockedAlert) &&
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
                new Scenario(OpenLandingPage.class, OpenLandingPage),
                new Scenario(OpenHomePage.class, OpenHomePage),
                new Scenario(OpenDetailsPage.class, OpenDetailsPage)
        ], TestClass, filterStrategyMock, [:])

        //then
        Journey journey;

        //journey with the OpenDetailsPage repeated twice.
        journey = journeys.find {
            List<Scenario> steps = new ArrayList(it.steps)
            return steps.size() == 5 &&
                    steps.remove(0) == new Scenario(OpenLandingPage.class, OpenLandingPage) &&
                    steps.remove(0) == new Scenario(OpenHomePage.class, OpenHomePage) &&
                    steps.remove(0) == new Scenario(OpenDetailsPage.class, OpenDetailsPage) &&
                    steps.remove(0) == new Scenario(OpenHomePage.class, OpenHomePage) &&
                    steps.remove(0) == new Scenario(OpenDetailsPage.class, OpenDetailsPage) &&
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
                    new Scenario(OpenHomePage.class, OpenHomePage),
                    new Scenario(OpenDetailsPageThatDoesntTerminate.class, OpenDetailsPageThatDoesntTerminate),
                    new Scenario(OpenDetailsPage.class, OpenDetailsPage)
            ], TestClass, filterStrategyMock, [:])

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
                    new Scenario(A.class, A),
                    new Scenario(B.class, B)
            ], TestClass, filterStrategyMock, [:])

            assert false: "An infinite loop exists, so an exception is expected"
        } catch (CascadeException e) {
            //then
            assert e.message.contains('Invalid configuration')
        }
    }

    @Test
    def void "given a set of non-termnating steps, with no downstream terminators, the generator should throw an exception"() {
        //re-entrant scenarioClasses are scenarioClasses that can occur multiple times in a journey, but will
        when(filterStrategyMock.match(any(Journey))).thenReturn(true)

        try {
            //when
            backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                    new Scenario(A.class, A),
                    new Scenario(B.class, B),
                    new Scenario(C.class, C)
            ], TestClass, filterStrategyMock, [:])

            assert false: "An infinite loop exists, so an exception is expected"
        } catch (CascadeException e) {
            //then
            assert e.message.contains('Invalid configuration')
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
