package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.onlinebankingexample.Utilities.assertElementPresent;
import static uk.co.malbec.onlinebankingexample.Utilities.waitForPage;

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
