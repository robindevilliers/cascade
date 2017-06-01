package com.github.robindevilliers.onlinebankingexample.steps;


import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.WebDriver;

import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

@SuppressWarnings("all")
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
        public void then() {
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
        public void then() {
            assertElementPresent(webDriver, "[test-form-login]");
            assertElementDisplayed(webDriver, "[test-dialog-authentication-failure]");
        }
    }
}