package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step(EnterPlea.Innocent.class)
@Narrative("Plea confirmation question.")
public interface EnterPleaAgain {

    @Narrative("Enter yes again as the plea and go to the sally page.")
    class InnocentAgain implements EnterPleaAgain {
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
            assertEquals("Welcome to Hell | Final Innocence Challenge", webDriver.getTitle());
        }
    }

    @Narrative("Enter no again as the plea and go to the sins declaration page.")
    class GuiltyFinally implements EnterPleaAgain {
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
            assertEquals("Welcome to Hell | Sins Declaration", webDriver.getTitle());
        }
    }
}
