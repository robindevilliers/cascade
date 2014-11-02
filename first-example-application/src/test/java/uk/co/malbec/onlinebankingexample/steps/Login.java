package uk.co.malbec.onlinebankingexample.steps;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.annotations.Terminator;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static uk.co.malbec.onlinebankingexample.Utilities.click;
import static uk.co.malbec.onlinebankingexample.Utilities.enterText;
import static uk.co.malbec.onlinebankingexample.Utilities.waitForPage;

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
        public void then(Throwable f){
            assertNull(f);
            assertEquals(1, webDriver.findElements(By.cssSelector("[test-form-challenge]")).size());
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
        public void when(){
            enterText(webDriver, "[test-field-username]", username);
            enterText(webDriver, "[test-field-password]", "invalidpassword");
            click(webDriver, "[test-cta-signin]");
            waitForPage(webDriver);
        }

        @Then
        public void then(Throwable f){
            assertNull(f);
            assertEquals(1, webDriver.findElements(By.cssSelector("[test-form-login]")).size());

            assertTrue(webDriver.findElement(By.cssSelector("[test-dialog-authentication-failure]")).isDisplayed());
        }

    }


}
