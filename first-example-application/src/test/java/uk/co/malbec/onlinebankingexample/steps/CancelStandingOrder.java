package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import java.util.List;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

//@Step(OpenPaymentsPage.class)
@ReEntrantTerminator(1)
public class CancelStandingOrder {

    @Demands
    private WebDriver webDriver;

    @Demands
    List<String[]> expectedStandingOrders;

    @When
    public void when() {
        click(webDriver, "[test-standing-order-row-0] [test-cancel-cta]");
        waitForPage(webDriver);

        expectedStandingOrders.remove(0);
    }

    @Then
    public void then(Throwable f) {
        assertNull(f);
        int i = 0;
        for (String[] expected: expectedStandingOrders){
            assertStandingOrderRow(webDriver, "" + i, expected[0], expected[1] , expected[2],expected[3] ,expected[4] ,expected[5], expected[6]);
            i++;
        }
    }
}