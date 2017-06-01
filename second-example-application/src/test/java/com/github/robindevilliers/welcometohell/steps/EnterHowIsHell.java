package com.github.robindevilliers.welcometohell.steps;


import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step({Jurisdiction.Hindu.class, Jurisdiction.Buddhist.class})
@Narrative("Enter how is hell level and go to the escaped page.")
public class EnterHowIsHell {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        Utilities.enterText(webDriver, "input[type='number']", "-15");

        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Escape", webDriver.getTitle());
    }
}
