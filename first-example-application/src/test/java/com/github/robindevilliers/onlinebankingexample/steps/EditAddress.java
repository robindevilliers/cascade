package com.github.robindevilliers.onlinebankingexample.steps;

import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.onlinebankingexample.domain.PersonalDetails;

import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

@Step(OpenEditAddress.class)
public class EditAddress {

    @Demands
    public WebDriver webDriver;

    @Demands
    public PersonalDetails personalDetails;

    @When
    public void when() {
        enterText(webDriver, "[test-input-address]", "15 Plane Road, RudeWay, ImaginaryVille, OPP002");
        click(webDriver, "[test-save-cta]");
        waitForPage(webDriver);

        personalDetails.setAddress("15 Plane Road, RudeWay, ImaginaryVille, OPP002");
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-field-address]", personalDetails.getAddress());
    }
}
