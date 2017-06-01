package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step({EnterPlea.Guilty.class, EnterPleaAgain.GuiltyFinally.class, SallyQuestion.YesToPinchingSally.class})
@Narrative("Enter sins and go to the date of death page.")
public class DeclareSins {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        Utilities.enterText(webDriver, "input[name='sin-1']", "I like barbarella.");
        Utilities.enterText(webDriver, "input[name='sin-2']", "I had chocolate 2 months in a row.");

        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Date of Death", webDriver.getTitle());
    }
}
