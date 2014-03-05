package uk.co.malbec.cascade

import org.junit.Before
import org.junit.Test
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.annotations.Terminator
import uk.co.malbec.cascade.model.Journey

class StepBackwardsFromTerminatorsJourneyGeneratorTest {

    StepBackwardsFromTerminatorsJourneyGenerator backwardsFromTerminatorsJourneyGenerator;

    @Before
    def void "initialisation"() {
        backwardsFromTerminatorsJourneyGenerator = new StepBackwardsFromTerminatorsJourneyGenerator()
    }

    @Test
    def void "given a set of dependent steps, the journey generator should generate journeys"() {

        List<Journey> journeys = backwardsFromTerminatorsJourneyGenerator.generateJourneys([
                OpenLoginPage,
                BadPassword,
                Successful,
                PostLoginAlert.UserAccountLockedAlert,
                PostLoginAlert.InformationAlert,
                DisplayUserHomePage
        ], TestClass)

        assert journeys.size() == 6

        Journey one = journeys[0]
        assert one.steps[0] == OpenLoginPage
        assert one.steps[1] == BadPassword
        assert one.steps[2] == Successful
        assert one.steps[3] == DisplayUserHomePage
        assert one.steps[4] == null

        Journey two = journeys[1]
        assert two.steps[0] == OpenLoginPage
        assert two.steps[1] == BadPassword
        assert two.steps[2] == Successful
        assert two.steps[3] == PostLoginAlert.InformationAlert
        assert two.steps[4] == DisplayUserHomePage
        assert two.steps[5] == null

        Journey three = journeys[2]
        assert three.steps[0] == OpenLoginPage
        assert three.steps[1] == BadPassword
        assert three.steps[2] == Successful
        assert three.steps[3] == PostLoginAlert.UserAccountLockedAlert
        assert three.steps[4] == null

        Journey four = journeys[3]
        assert four.steps[0] == OpenLoginPage
        assert four.steps[1] == Successful
        assert four.steps[2] == DisplayUserHomePage
        assert four.steps[3] == null

        Journey five = journeys[4]
        assert five.steps[0] == OpenLoginPage
        assert five.steps[1] == Successful
        assert five.steps[2] == PostLoginAlert.InformationAlert
        assert five.steps[3] == DisplayUserHomePage
        assert five.steps[4] == null

        Journey six = journeys[5]
        assert six.steps[0] == OpenLoginPage
        assert six.steps[1] == Successful
        assert six.steps[2] == PostLoginAlert.UserAccountLockedAlert
        assert six.steps[3] == null
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
    static class DisplayUserHomePage{

    }

    public static class TestClass {
    }

}
