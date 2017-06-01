package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step({SelectPayment.HeavenlyHalfPennies.class, SelectPayment.Gold.class, SelectPayment.Nothing.class, SelectPayment.EarthlyFiat.class})
@Narrative("Appeal question.")
public interface AppealQuestion {
    @Narrative("Enter yes to appeal and go to thrall page.")
    class YesToAppeal implements AppealQuestion {
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
            assertEquals("Welcome to Hell | Thrall of Hell", webDriver.getTitle());
        }
    }

    @Narrative("Enter no to appeal and go to thrall page.")
    class NoToAppeal implements AppealQuestion {
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
            assertEquals("Welcome to Hell | Thrall of Hell", webDriver.getTitle());
        }
    }
}
