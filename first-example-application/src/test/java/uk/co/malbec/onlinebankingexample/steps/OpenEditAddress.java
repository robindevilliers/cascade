package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;
import uk.co.malbec.onlinebankingexample.domain.PersonalDetails;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPersonalPage.class)
public class OpenEditAddress {

    @Demands
    public WebDriver webDriver;

    @Demands
    public PersonalDetails personalDetails;

    @When
    public void when() {
        click(webDriver, "[test-edit-address-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-current-address-text]", personalDetails.getAddress());
    }
}
