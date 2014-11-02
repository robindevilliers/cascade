package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.annotations.Terminator;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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
            webDriver.findElement(By.cssSelector("[test-field-username]")).sendKeys(username);
            webDriver.findElement(By.cssSelector("[test-field-password]")).sendKeys(password);
            //TODO - think there may be a race condition at this point.  Between send keys and the signin click.
            webDriver.findElement(By.cssSelector("[test-cta-signin]")).click();
            new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
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
            webDriver.findElement(By.cssSelector("[test-field-username]")).sendKeys(username);
            webDriver.findElement(By.cssSelector("[test-field-password]")).sendKeys("invalidpassword");
            webDriver.findElement(By.cssSelector("[test-cta-signin]")).click();
            new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
        }

        @Then
        public void then(Throwable f){
            assertNull(f);
            assertEquals(1, webDriver.findElements(By.cssSelector("[test-form-login]")).size());

            assertTrue(webDriver.findElement(By.cssSelector("[test-dialog-authentication-failure]")).isDisplayed());
        }

    }


}
