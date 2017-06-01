package com.github.robindevilliers.welcometohell.steps;


import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("all")
@Step(OpenLandingPage.class)
@Narrative("Open accesibility page.")
public class OpenAccessibilityPage {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        webDriver.findElements(By.cssSelector(".tst-link"))
                .stream()
                .filter(w -> w.getText().equals("Accessibility Options"))
                .findFirst()
                .ifPresent(w -> w.click());

        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Accessibility", webDriver.getTitle());
    }
}
