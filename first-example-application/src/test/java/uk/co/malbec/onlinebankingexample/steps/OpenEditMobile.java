package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPersonalPage.class)
@ReEntrantTerminator(1)
public class OpenEditMobile {

    @Demands
    public WebDriver webDriver;

    @When
    public void when() {
        click(webDriver, "[test-edit-mobile-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-current-mobile-text]", "0788 1234 567");
    }
}
