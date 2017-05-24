package uk.co.malbec.cascade.model

import org.junit.Test
import uk.co.malbec.cascade.Scenario
import uk.co.malbec.cascade.annotations.Narrative
import uk.co.malbec.cascade.annotations.Scan
import uk.co.malbec.cascade.annotations.Step

class JourneyTest {

    @Test
    def void "given a Journey, it should have a name and contains appropriate steps"(){

        Journey journey = new Journey([new Scenario(GoToLoginPage.class, GoToLoginPage), new Scenario(EnterPassword.class, EnterPassword)], TestClass)
        journey.init(1)
        assert journey.name == 'Test[1]  JourneyTest$GoToLoginPage  JourneyTest$EnterPassword '
        assert journey.steps == [new Scenario(GoToLoginPage.class, GoToLoginPage), new Scenario(EnterPassword.class, EnterPassword)]
        assert journey.description.displayName == 'Test[1]  JourneyTest$GoToLoginPage  JourneyTest$EnterPassword (uk.co.malbec.cascade.model.JourneyTest$TestClass)'
    }

    @Step
    public static class GoToLoginPage {

    }

    @Step
    public static class EnterPassword {

    }

    @Step
    @Narrative("User goes to the 'forgot password' page ")
    public static class GoToForgotPasswordPage {

    }

    @Step
    @Narrative("and he enters his email address")
    public static class EnterEmailAddress {

    }

    @Scan(["uk.co.this", "uk.co.that"])
    public static class TestClass {

    }
}
