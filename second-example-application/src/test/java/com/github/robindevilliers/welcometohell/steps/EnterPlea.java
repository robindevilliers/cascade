package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;
import static com.github.robindevilliers.welcometohell.Utilities.selectOption;
import static com.github.robindevilliers.welcometohell.Utilities.waitForPage;

@Step({
        Jurisdiction.Jewish.class,
        Jurisdiction.Christian.class,
        Jurisdiction.Pirate.class
})
@Narrative("Plea question.")
public interface EnterPlea {

    @Narrative("Enter innocent as the plea and go to the plea confirmation page.")
    class Innocent implements EnterPlea {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            selectOption(webDriver, "input", "true");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration Again", webDriver.getTitle());
        }
    }

    @Narrative("Enter guilty as the plea and go to the sins declaration page.")
    class Guilty implements EnterPlea {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            selectOption(webDriver, "input", "false");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Sins Declaration", webDriver.getTitle());
        }
    }
}
