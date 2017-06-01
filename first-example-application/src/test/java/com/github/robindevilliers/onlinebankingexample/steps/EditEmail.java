package com.github.robindevilliers.onlinebankingexample.steps;

import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.onlinebankingexample.domain.PersonalDetails;

import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

@Step(OpenEditEmail.class)
@ReEntrantTerminator(1)
public class EditEmail {

    @Demands
    public WebDriver webDriver;

    @Demands
    public PersonalDetails personalDetails;

    @When
    public void when() {
        enterText(webDriver, "[test-input-email]", "anne@theoreticalcity.co.uk");
        click(webDriver, "[test-save-cta]");
        waitForPage(webDriver);

        personalDetails.setEmail("anne@theoreticalcity.co.uk");
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-field-email]", personalDetails.getEmail());
    }
}
