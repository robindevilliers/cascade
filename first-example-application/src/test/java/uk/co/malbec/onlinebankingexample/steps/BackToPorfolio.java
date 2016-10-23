package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

//@Step({EditAddress.class, EditEmail.class, EditMobile.class, OpenAccountPage.class, SetupStandingOrder.class})
public class BackToPorfolio {

    @Demands
    public WebDriver webDriver;

    @When
    public void when() {
        click(webDriver, "[test-link-portfolio]");
        waitForPage(webDriver);
    }
}
