package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(AppealQuestion.class)
@Narrative("Thrall question.")
public interface ThrallQuestion {

    @Narrative("Enter yes to apply to be a thrall and go to the thrall results page.")
    class YesToThrall implements ThrallQuestion {
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
            assertEquals("Welcome to Hell | Thrall Results", webDriver.getTitle());
        }
    }

    @Narrative("Enter no to apply to be a thrall and go to the booking page.")
    class NoToThrall implements ThrallQuestion {
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
            assertEquals("Welcome to Hell | Booking", webDriver.getTitle());
        }
    }
}
