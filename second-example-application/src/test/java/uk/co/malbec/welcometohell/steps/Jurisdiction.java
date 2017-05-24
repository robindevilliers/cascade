package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(BalletQuestion.HatesBallet.class)
@Narrative("Jurisdiction question.")
public interface Jurisdiction {

    @Narrative("Enter muslim for religion and go to the muslim conclusion page.")
    class Muslim implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            selectOption(webDriver, "input", "MUSLIM");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
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
            selectOption(webDriver, "input", "HINDU");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
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
            selectOption(webDriver, "input", "BUDDHIST");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
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
            selectOption(webDriver, "input", "JEWISH");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
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
            selectOption(webDriver, "input", "PIRATE");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration", webDriver.getTitle());
        }
    }

    @Narrative("Enter jedi for religion and go to the next page depending on gender.")
    class Jedi implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @Demands
        private String gender;

        @When
        public void when() {
            selectOption(webDriver, "input", "JEDI");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            if (gender.equals("MALE")){
                assertEquals("Welcome to Hell | Star Wars", webDriver.getTitle());
            } else {
                assertEquals("Welcome to Hell | Cosplay", webDriver.getTitle());
            }
        }
    }

    @Narrative("Enter christian for religion and go to the plea page.")
    class Christian implements Jurisdiction {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            selectOption(webDriver, "input", "CHRISTIAN");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration", webDriver.getTitle());
        }
    }
}
