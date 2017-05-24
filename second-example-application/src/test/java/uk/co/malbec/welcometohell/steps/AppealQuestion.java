package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step({SelectPayment.HeavenlyHalfPennies.class, SelectPayment.Gold.class, SelectPayment.Nothing.class, SelectPayment.EarthlyFiat.class})
@Narrative("Appeal question.")
public interface AppealQuestion {
    @Narrative("Enter yes to appeal and go to thrall page.")
    class YesToAppeal implements AppealQuestion {
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
            assertEquals("Welcome to Hell | Thrall of Hell", webDriver.getTitle());
        }
    }

    @Narrative("Enter no to appeal and go to thrall page.")
    class NoToAppeal implements AppealQuestion {
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
            assertEquals("Welcome to Hell | Thrall of Hell", webDriver.getTitle());
        }
    }
}
