package com.github.robindevilliers.onlinebankingexample.steps;

import com.github.robindevilliers.onlinebankingexample.Utilities;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.annotations.Demands;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.annotations.Then;
import com.github.robindevilliers.cascade.annotations.When;
import com.github.robindevilliers.onlinebankingexample.domain.PersonalDetails;

@Step(OpenPersonalPage.class)
public class OpenEditEmail {
    @Demands
    public WebDriver webDriver;

    @Demands
    public PersonalDetails personalDetails;

    @When
    public void when(){
        Utilities.click(webDriver, "[test-edit-email-cta]");
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {
        Utilities.assertTextEquals(webDriver, "[test-current-email-text]", personalDetails.getEmail());
    }
}
