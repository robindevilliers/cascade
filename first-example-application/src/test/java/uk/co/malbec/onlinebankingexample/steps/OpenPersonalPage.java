package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(Portfolio.class)
public class OpenPersonalPage {

    @Demands
    public WebDriver webDriver;

    @Supplies
    public Map<String, String> personalDetails;

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
    public void then(Throwable f) {
        assertNull(f);
        assertTextEquals(webDriver, "[test-field-name]", "Robin de Villiers");
        assertTextEquals(webDriver, "[test-field-nationality]", "British");
        assertTextEquals(webDriver, "[test-field-domicile]", "UK");
        assertTextEquals(webDriver, "[test-field-address]", "7 Special Way, FairBank, ImaginaryVille, WOW007");
        assertTextEquals(webDriver, "[test-field-mobile]", "0788 1234 567");
        assertTextEquals(webDriver, "[test-field-email]", "robin@imaginaryville.co.uk");
    }

}
