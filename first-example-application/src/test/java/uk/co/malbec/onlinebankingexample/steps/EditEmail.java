package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.utils.Reference;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenEditEmail.class)
@ReEntrantTerminator(1)
public class EditEmail {

    @Demands
    public WebDriver webDriver;

    @Demands
    public Reference<Boolean> emailHasBeenEdited;

    @When
    public void when() {
        enterText(webDriver, "[test-input-email]", "robin@theoreticalcity.co.uk");
        click(webDriver, "[test-save-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-field-email]", "robin@theoreticalcity.co.uk");
        emailHasBeenEdited.set(true);
    }
}
