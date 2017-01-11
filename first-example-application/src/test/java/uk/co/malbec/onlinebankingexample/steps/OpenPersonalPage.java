package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.utils.Reference;

import java.util.HashMap;
import java.util.Map;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(Portfolio.class)
public class OpenPersonalPage {

    @Demands
    public WebDriver webDriver;

    @Supplies
    public Map<String, String> personalDetails;

    @Supplies
    public Reference<Boolean> addressHasBeenEdited = new Reference<Boolean>(false);

    @Supplies
    public Reference<Boolean> emailHasBeenEdited = new Reference<Boolean>(false);

    @Supplies
    public Reference<Boolean> mobileHasBeenEdited = new Reference<Boolean>(false);

    @Given
    public void given() {
        personalDetails = new HashMap<String, String>() {{
            put("name", "Robin de Villiers");
            put("nationality", "British");
            put("domicile", "UK");
            put("address", "7 Special Way, FairBank, ImaginaryVille, WOW007");
            put("mobile", "0788 1234 567");
            put("email", "robin@imaginaryville.co.uk");
        }};
    }

    @When
    public void when() {
        click(webDriver, "[test-link-personal-details]");
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertTextEquals(webDriver, "[test-field-name]", "Robin de Villiers");
        assertTextEquals(webDriver, "[test-field-nationality]", "British");
        assertTextEquals(webDriver, "[test-field-domicile]", "UK");
        if (addressHasBeenEdited.get()) {
            assertTextEquals(webDriver, "[test-field-address]", "15 Plane Road, RudeWay, ImaginaryVille, OPP002");
        } else {
            assertTextEquals(webDriver, "[test-field-address]", "7 Special Way, FairBank, ImaginaryVille, WOW007");
        }

        if (mobileHasBeenEdited.get()){
            assertTextEquals(webDriver, "[test-field-mobile]", "0789 1234 7765");
        } else {
            assertTextEquals(webDriver, "[test-field-mobile]", "0788 1234 567");
        }

        if (emailHasBeenEdited.get()) {
            assertTextEquals(webDriver, "[test-field-email]", "robin@theoreticalcity.co.uk");
        } else {
            assertTextEquals(webDriver, "[test-field-email]", "robin@imaginaryville.co.uk");
        }

    }

}
