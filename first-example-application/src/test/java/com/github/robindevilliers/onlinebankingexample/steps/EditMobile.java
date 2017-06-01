package com.github.robindevilliers.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.annotations.Demands;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.annotations.Then;
import com.github.robindevilliers.cascade.annotations.When;
import com.github.robindevilliers.onlinebankingexample.domain.PersonalDetails;

import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

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
