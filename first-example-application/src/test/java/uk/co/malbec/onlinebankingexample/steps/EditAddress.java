package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenEditAddress.class)
public class EditAddress {

    @Demands
    public WebDriver webDriver;

    @When
    public void when() {
        enterText(webDriver, "[test-input-address]", "15 Plane Road, RudeWay, ImaginaryVille, OPP002");
        click(webDriver, "[test-save-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then(Throwable f) {
        assertNull(f);
        assertTextEquals(webDriver, "[test-field-address]", "15 Plane Road, RudeWay, ImaginaryVille, OPP002");
    }
}
