package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.annotations.Terminator;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenLandingPage.class)
public interface Login {
    public class SuccessfulLogin implements Login {

        @Supplies
        private String username = "anne";
        @Supplies
        private String password = "other";

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            enterText(webDriver, "[test-field-username]", username);
            enterText(webDriver, "[test-field-password]", password);
            click(webDriver, "[test-cta-signin]");
            waitForPage(webDriver);
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            assertElementPresent(webDriver, "[test-form-challenge]");
        }
    }

    @Terminator
    public class FailedLogin implements Login {

        @Supplies
        private String username = "anne";

        @Supplies
        private String password = "mykey";

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            enterText(webDriver, "[test-field-username]", username);
            enterText(webDriver, "[test-field-password]", "invalidpassword");
            click(webDriver, "[test-cta-signin]");
            waitForPage(webDriver);
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            assertElementPresent(webDriver, "[test-form-login]");
            assertElementDisplayed(webDriver, "[test-dialog-authentication-failure]");
        }
    }
}