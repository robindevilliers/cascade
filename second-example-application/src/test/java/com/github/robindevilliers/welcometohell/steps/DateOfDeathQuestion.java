package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step({SallyQuestion.NoToPinchingSally.class, DeclareSins.class})
@Narrative("Enter date of death and go to the payment page.")
public class DateOfDeathQuestion {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        Utilities.enterText(webDriver, "input[type='date']", "01/01/2000");

        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Time to Pay", webDriver.getTitle());
    }
}
