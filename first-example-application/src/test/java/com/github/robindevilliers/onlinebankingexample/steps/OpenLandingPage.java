package com.github.robindevilliers.onlinebankingexample.steps;


import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;
import static com.github.robindevilliers.onlinebankingexample.Utilities.assertElementPresent;
import static com.github.robindevilliers.onlinebankingexample.Utilities.waitForPage;

@SuppressWarnings("all")
@Step
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
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Tabby Banking", webDriver.getTitle());
        assertElementPresent(webDriver, "[test-form-login]");
    }

    @Clear
    public void clear(){
        webDriver.close();
    }

}
