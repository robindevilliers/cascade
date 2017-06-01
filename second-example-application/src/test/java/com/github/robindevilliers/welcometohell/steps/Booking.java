package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step({ThrallResults.class, ThrallQuestion.NoToThrall.class})
@Narrative("Mother in law question.")
public interface Booking {
    @Narrative("Enter yes to mother in law and go to the missed booking page.")
    class YesToMotherInLaw implements Booking {

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "true");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Missed Booking", webDriver.getTitle());
        }
    }
    @Narrative("Enter no to mother in law and go to the missed booking page.")
    class NoToMotherInLaw implements Booking {

        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "false");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Missed Booking", webDriver.getTitle());
        }
    }

}
