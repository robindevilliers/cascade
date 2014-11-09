package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPersonalPage.class)
public class OpenEditMobile {

    @Demands
    public WebDriver webDriver;

    @When
    public void when() {
        click(webDriver, "[test-edit-mobile-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then(Throwable f) {
        assertNull(f);
        assertTextEquals(webDriver, "[test-current-mobile-text]", "0788 1234 567");
    }
}
