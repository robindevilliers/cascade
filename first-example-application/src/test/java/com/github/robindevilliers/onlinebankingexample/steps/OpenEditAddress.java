package com.github.robindevilliers.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.annotations.Demands;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.annotations.Then;
import com.github.robindevilliers.cascade.annotations.When;
import com.github.robindevilliers.onlinebankingexample.domain.PersonalDetails;

import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

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
