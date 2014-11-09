package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPaymentsPage.class)
public class CancelStandingOrder {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        click(webDriver, "[test-standing-order-row-0] [test-cancel-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then(Throwable f) {
        assertNull(f);
        assertStandingOrderRow(webDriver, "0", "Sed consequat", "X12345-ROBIN-DE-VILLIERS", "44433232", "893810", "30 Sep 2014", "Quarterly", "£ 15.00");
        assertStandingOrderRow(webDriver, "1", "Etiam sit amet", "11102929920293837X", "23334332", "100210", "02 Dec 2014", "Yearly", "£ 85.00");
    }
}