package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import uk.co.malbec.cascade.annotations.*;

import static uk.co.malbec.onlinebankingexample.Utilities.waitForPage;

@Step
public class OpenLandingPage {

    @Supplies
    private WebDriver webDriver;

    @Given
    public void given() {
        webDriver = new FirefoxDriver();
    }

    @When
    public void when() {
        webDriver.get("http://localhost:8080");
        waitForPage(webDriver);
    }

    @Clear
    public void clear(){
        webDriver.close();
    }

}
