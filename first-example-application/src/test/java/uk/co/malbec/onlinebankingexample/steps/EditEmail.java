package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenEditEmail.class)
public class EditEmail {

    @Demands
    public WebDriver webDriver;

    @When
    public void when() {
        enterText(webDriver, "[test-input-email]", "robin@theoreticalcity.co.uk");
        click(webDriver, "[test-save-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then(Throwable f) {
        assertNull(f);
        assertTextEquals(webDriver, "[test-field-email]", "robin@theoreticalcity.co.uk");
    }
}
