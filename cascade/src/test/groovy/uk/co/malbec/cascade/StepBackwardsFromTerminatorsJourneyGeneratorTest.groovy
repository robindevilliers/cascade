package uk.co.malbec.cascade

import org.junit.Before
import org.junit.Test
import uk.co.malbec.cascade.annotations.OnlyRunWith
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.annotations.Terminator
import uk.co.malbec.cascade.model.Journey

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
        assert journey : "expected a journey successful password challenge followed by the account list page showing mortgage"
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
        assert journey : "expected a journey successful password challenge followed by the account list page showing saver account and then the saver account page"
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
        assert journey : "expected a journey successful password challenge followed by an informational message and then the mortgage list page"
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
        assert journey : "expected a journey successful password challenge followed by an informational message and then the saver list page, and then the saver details page"
        journeys.remove(journey)

        // we should have the journey that has the account locked alert which is a terminating journey
        journey = journeys.find {
            List<Class> steps = new ArrayList(it.steps)
            return steps.remove(0) == OpenLoginPage &&
                    steps.remove(0) == Successful &&
                    steps.remove(0) == PostLoginAlert.UserAccountLockedAlert &&
                    steps.empty
        }
        assert journey : "expected a journey successful password challenge and a message stating that the account is locked"
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
        assert journey : "expected a journey with a failed password, followed by a success and then the mortgage list page."
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
        assert journey : "expected a journey with a failed password, followed by a success and then the saver list page and then the saver details."
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
        assert journey : "expected a journey with a failed password, followed by a success, an information message and then the mortgage list page."
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
        assert journey : "expected a journey with a failed password, followed by a success, an information message and then the saver list page and then the saver details page."
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
        assert journey : "expected a journey with a failed password, followed by a success and a message stating that the account is locked"
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
    @OnlyRunWith(DisplayAccountsList.SaverAccount)
    static class DisplaySaverAccount{

    }

    public static class TestClass {
    }

}
