package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step({
        Jurisdiction.Jewish.class,
        Jurisdiction.Christian.class,
        Jurisdiction.Pirate.class,
        CosPlayQuestion.NoToCosPlay.class,
        StarWarsQuestion.IgnorantStarWars.class
})
public interface EnterPlea {

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
