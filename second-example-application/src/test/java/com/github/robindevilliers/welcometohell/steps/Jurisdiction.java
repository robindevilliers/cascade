package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step(BalletQuestion.HatesBallet.class)
@Narrative("Jurisdiction question.")
public interface Jurisdiction {

    @Narrative("Enter muslim for religion and go to the muslim conclusion page.")
    class Muslim implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "MUSLIM");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Get Out Of Here", webDriver.getTitle());
        }
    }

    @Narrative("Enter hindu for religion and go to the how is hell page.")
    class Hindu implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "HINDU");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | How is Hell", webDriver.getTitle());
        }
    }

    @Narrative("Enter buddhist for religion and go to the how is hell page.")
    class Buddhist implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "BUDDHIST");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | How is Hell", webDriver.getTitle());
        }
    }

    @Narrative("Enter jewish for religion and go to the how is plea page.")
    class Jewish implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "JEWISH");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration", webDriver.getTitle());
        }
    }

    @Narrative("Enter pirate for religion and go to the how is plea page.")
    class Pirate implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "PIRATE");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration", webDriver.getTitle());
        }
    }

    @Narrative("Enter christian for religion and go to the plea page.")
    class Christian implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", "CHRISTIAN");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration", webDriver.getTitle());
        }
    }
}
