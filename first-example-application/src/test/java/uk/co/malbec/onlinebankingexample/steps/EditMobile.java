package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;
import uk.co.malbec.onlinebankingexample.domain.PersonalDetails;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenEditMobile.class)
public class EditMobile {

    @Demands
    public WebDriver webDriver;

    @Demands
    public PersonalDetails personalDetails;

    @When
    public void when() {
        enterText(webDriver, "[test-input-mobile]", "0789 1234 7765");
        click(webDriver, "[test-save-cta]");
        waitForPage(webDriver);

        personalDetails.setMobile("0789 1234 7765");
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-field-mobile]", personalDetails.getMobile());
    }
}
