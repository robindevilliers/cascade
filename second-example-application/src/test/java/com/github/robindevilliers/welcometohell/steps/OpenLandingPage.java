package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("all")
@Step
@Narrative("Open application.")
public class OpenLandingPage {
    @Supplies
    private WebDriver webDriver;

    @Given
    public void given() {
        webDriver = new ChromeDriver();
    }

    @When
    public void when() {
        webDriver.get("http://localhost:8080");
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Welcome", webDriver.getTitle());
    }

    @Clear
    public void clear(){
        webDriver.close();
    }
}
