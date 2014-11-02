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

@Step(Login.class)
public interface Challenge {


    public class PassChallenge implements Challenge {

        @Supplies
        private String challengePhrase = "onceuponamidnightdreary";

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {

            String textOne = webDriver.findElement(By.cssSelector("[test-text-number-one]")).getText();
            String textTwo = webDriver.findElement(By.cssSelector("[test-text-number-two]")).getText();
            String textThree = webDriver.findElement(By.cssSelector("[test-text-number-three]")).getText();

            webDriver.findElement(By.cssSelector("[test-field-number-one]")).sendKeys("" + challengePhrase.charAt(new Integer(textOne) - 1));
            webDriver.findElement(By.cssSelector("[test-field-number-two]")).sendKeys("" + challengePhrase.charAt(new Integer(textTwo) - 1));
            webDriver.findElement(By.cssSelector("[test-field-number-three]")).sendKeys("" + challengePhrase.charAt(new Integer(textThree) - 1));

            webDriver.findElement(By.cssSelector("[test-cta-authenticate]")).click();

            new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
        }
    }

    @Terminator
    public class FailChallenge implements Challenge {
        @Supplies
        private String challengePhrase = "onceuponamidnightdreary";

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {


            webDriver.findElement(By.cssSelector("[test-field-number-one]")).sendKeys("a");
            webDriver.findElement(By.cssSelector("[test-field-number-two]")).sendKeys("a");
            webDriver.findElement(By.cssSelector("[test-field-number-three]")).sendKeys("a");

            webDriver.findElement(By.cssSelector("[test-cta-authenticate]")).click();

            new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            assertEquals(1, webDriver.findElements(By.cssSelector("[test-form-login]")).size());
            assertTrue(webDriver.findElement(By.cssSelector("[test-dialog-authentication-failure]")).isDisplayed());

        }

    }


}
