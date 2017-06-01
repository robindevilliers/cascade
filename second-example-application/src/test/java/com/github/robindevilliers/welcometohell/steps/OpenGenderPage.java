package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;


@Step(OpenLandingPage.class)
@Narrative("Start and go to gender page.")
public class OpenGenderPage {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Gender", webDriver.getTitle());
    }

}
