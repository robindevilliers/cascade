package com.github.robindevilliers.onlinebankingexample.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.onlinebankingexample.Utilities;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.onlinebankingexample.domain.PersonalDetails;

@Step(OpenPersonalPage.class)
@ReEntrantTerminator(1)
public class OpenEditMobile {

    @Demands
    public WebDriver webDriver;

    @Demands
    public PersonalDetails personalDetails;

    @When
    public void when() {
        Utilities.click(webDriver, "[test-edit-mobile-cta]");
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        Utilities.assertTextEquals(webDriver, "[test-current-mobile-text]", personalDetails.getMobile());
    }
}
