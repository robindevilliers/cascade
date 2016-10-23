package uk.co.malbec.onlinebankingexample.pages;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Page;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGeneratorTest;
import uk.co.malbec.onlinebankingexample.steps.OpenLandingPage;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static uk.co.malbec.onlinebankingexample.Utilities.assertElementPresent;

@Page(OpenLandingPage.class)
public class LandingPage {

    @Demands
    public WebDriver webDriver;

    @Then
    //TODO - when throwable argument is not supplied, then there must be a helpful error message
    //TODO - if throwable not supplied here, then we don't expect an exception??  likewise when there is no then clause
    public void then(Throwable f) {
        assertNull(f);
        assertEquals("Tabby Banking", webDriver.getTitle());
        assertElementPresent(webDriver, "[test-form-login]");
    }
}
