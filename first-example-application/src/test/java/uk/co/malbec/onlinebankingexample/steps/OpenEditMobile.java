package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.onlinebankingexample.domain.PersonalDetails;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPersonalPage.class)
@ReEntrantTerminator(1)
public class OpenEditMobile {

    @Demands
    public WebDriver webDriver;

    @Demands
    public PersonalDetails personalDetails;

    @When
    public void when() {
        click(webDriver, "[test-edit-mobile-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-current-mobile-text]", personalDetails.getMobile());
    }
}
