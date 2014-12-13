package uk.co.malbec.cascade.modules.generator

import org.junit.Before
import org.junit.Test
import uk.co.malbec.cascade.annotations.OnlyRunWith
import uk.co.malbec.cascade.annotations.ReEntrantTerminator
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.annotations.Terminator
import uk.co.malbec.cascade.conditions.ConditionalLogic
import uk.co.malbec.cascade.conditions.Predicate
import uk.co.malbec.cascade.exception.CascadeException
import uk.co.malbec.cascade.model.Journey
import uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator

import static uk.co.malbec.cascade.conditions.Predicates.withStep

class StepBackwardsFromTerminatorsJourneyGeneratorTest {

    StepBackwardsFromTerminatorsJourneyGenerator backwardsFromTerminatorsJourneyGenerator;

    @Before
    def void "initialisation"() {
        backwardsFromTerminatorsJourneyGenerator = new StepBackwardsFromTerminatorsJourneyGenerator(new ConditionalLogic())
    }

    @Test
    def void "given a set of dependent steps, the journey generator should generate journeys"() {

        //when
        List<Journey> journeys = backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                OpenLoginPage,
                BadPassword,
                Successful,
                PostLoginAlert.UserAccountLockedAlert,
                PostLoginAlert.InformationAlert,
                DisplayAccountsList.MortgageAccount,
                DisplayAccountsList.SaverAccount,
                DisplaySaverAccount
        ], TestClass)

        //then
        Journey journey;

        //we should have the simplest most happy journey for mortgages
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == DisplayAccountsList.MortgageAccount &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by the account list page showing mortgage"
        journeys.remove(journey)

        //we should have the simplest most happy journey for saver accounts
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == DisplayAccountsList.SaverAccount &&
                    steps.remove(0) == DisplaySaverAccount &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by the account list page showing saver account and then the saver account page"
        journeys.remove(journey)

        //we should have the journey that includes an optional informational message that does not terminate
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == PostLoginAlert.InformationAlert &&
                    steps.remove(0) == DisplayAccountsList.MortgageAccount &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by an informational message and then the mortgage list page"
        journeys.remove(journey)

        //we should have the journey that includes an optional informational message that does not terminate
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == PostLoginAlert.InformationAlert &&
                    steps.remove(0) == DisplayAccountsList.SaverAccount &&
                    steps.remove(0) == DisplaySaverAccount
            steps.empty
        }
        assert journey: "expected a journey successful password challenge followed by an informational message and then the saver list page, and then the saver details page"
        journeys.remove(journey)

        // we should have the journey that has the account locked alert which is a terminating journey
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == PostLoginAlert.UserAccountLockedAlert &&
                    steps.empty
        }
        assert journey: "expected a journey successful password challenge and a message stating that the account is locked"
        journeys.remove(journey)

        //we should have all the above tests preceded by an incorrect password entry
        //simplest journey first
        journey = journeys.find { it ->
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == BadPassword &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == DisplayAccountsList.MortgageAccount &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success and then the mortgage list page."
        journeys.remove(journey)

        //we should have all the above tests preceded by an incorrect password entry
        //simplest journey first
        journey = journeys.find { it ->
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == BadPassword &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == DisplayAccountsList.SaverAccount &&
                    steps.remove(0) == DisplaySaverAccount &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success and then the saver list page and then the saver details."
        journeys.remove(journey)

        //informational message with bad password
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == BadPassword &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == PostLoginAlert.InformationAlert &&
                    steps.remove(0) == DisplayAccountsList.MortgageAccount &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success, an information message and then the mortgage list page."
        journeys.remove(journey)

        //informational message with bad password
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == BadPassword &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == PostLoginAlert.InformationAlert &&
                    steps.remove(0) == DisplayAccountsList.SaverAccount &&
                    steps.remove(0) == DisplaySaverAccount &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success, an information message and then the saver list page and then the saver details page."
        journeys.remove(journey)

        //user account locked with bad password
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == BadPassword &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == PostLoginAlert.UserAccountLockedAlert &&
                    steps.empty
        }
        assert journey: "expected a journey with a failed password, followed by a success and a message stating that the account is locked"
        journeys.remove(journey)

        //if this is empty then we don't have any unexpected journeys.
        // notably, we don't have saver details steps following mortgage list which is prohibited by OnRunWith tag.
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
        //re-entrant steps are steps that can occur multiple times in a journey, but will

        //when
        List<Journey> journeys = backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                OpenLandingPage,
                OpenHomePage,
                OpenDetailsPage
        ], TestClass)

        //then
        Journey journey;

        //journey with the OpenDetailsPage repeated twice.
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.size() == 5 &&
                    steps.remove(0) == OpenLandingPage &&
                    steps.remove(0) == OpenHomePage &&
                    steps.remove(0) == OpenDetailsPage &&
                    steps.remove(0) == OpenHomePage &&
                    steps.remove(0) == OpenDetailsPage &&
                    steps.empty
        }
        assert journey: "expected a journey with the open details page occurring exactly twice and the journey should end on the open details page"
        journeys.remove(journey)

    }

    @Test
    def void "given a set of re-entrant steps with no re-entrant-terminators, the journey generator should throw an exception"() {
        //re-entrant steps are steps that can occur multiple times in a journey, but will
        try {
            //when
            backwardsFromTerminatorsJourneyGenerator.generateJourneys([

                    OpenHomePage,
                    OpenDetailsPageThatDoesntTerminate,
                    OpenDetailsPage
            ], TestClass)

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
        //re-entrant steps are steps that can occur multiple times in a journey, but will
        try {
            //when
            backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                    A,
                    B
            ], TestClass)

            assert false: "An infinite loop exists, so an exception is expected"
        } catch (CascadeException e) {
            //then
            assert e.message == 'Invalid configuration: Scenario class uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGeneratorTest$A not found in any journey: This journey generator calculates journeys by finding terminators and walking backwards to the steps that start journeys. If a step is not found in journeys, it is either dependent on steps that don\'t lead to a journey start, or there are no terminators downstream of this step.'
        }
    }

    @Test
    def void "given a set of non-termnating steps, with no downstream terminators, the generator should throw an exception"() {
        //re-entrant steps are steps that can occur multiple times in a journey, but will
        try {
            //when
            backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                    A,
                    B,
                    C
            ], TestClass)

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
