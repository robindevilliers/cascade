package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.onlinebankingexample.PersonalDetailsRendering;
import uk.co.malbec.onlinebankingexample.PersonalDetailsTransitionRendering;
import uk.co.malbec.onlinebankingexample.domain.PersonalDetails;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@SuppressWarnings("all")
@Step(Portfolio.class)
public class OpenPersonalPage {

    @Demands
    public WebDriver webDriver;

    @Supplies(stateRenderer = PersonalDetailsRendering.class, transitionRenderer = PersonalDetailsTransitionRendering.class)
    public PersonalDetails personalDetails;

    @Given
    public void given() {
        personalDetails = new PersonalDetails()
                .setName("Anne Other")
                .setNationality("British")
                .setDomicile("UK")
                .setAddress("7 Special Way, FairBank, ImaginaryVille, WOW007")
                .setMobile("0788 1234 567")
                .setEmail("anne@imaginaryville.co.uk");
    }

    @When
    public void when() {
        click(webDriver, "[test-link-personal-details]");
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-field-name]", personalDetails.getName());
        assertTextEquals(webDriver, "[test-field-nationality]", personalDetails.getNationality());
        assertTextEquals(webDriver, "[test-field-domicile]", personalDetails.getDomicile());
        assertTextEquals(webDriver, "[test-field-address]", personalDetails.getAddress());
        assertTextEquals(webDriver, "[test-field-mobile]", personalDetails.getMobile());
        assertTextEquals(webDriver, "[test-field-email]", personalDetails.getEmail());
    }

}
