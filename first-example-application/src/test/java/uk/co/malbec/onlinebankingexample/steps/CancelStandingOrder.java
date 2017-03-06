package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.onlinebankingexample.domain.StandingOrder;

import java.util.List;

import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPaymentsPage.class)
@ReEntrantTerminator(1)
public class CancelStandingOrder {

    @Demands
    private WebDriver webDriver;

    @Demands
    public List<StandingOrder> standingOrders;

    @When
    public void when() {
        click(webDriver, "[test-standing-order-row-0] [test-cancel-cta]");
        waitForPage(webDriver);

        standingOrders.remove(0);
    }

    @Then
    public void then() {
        for (int row = 0; row < standingOrders.size(); row++) {
            assertStandingOrderRow(webDriver, row, standingOrders.get(row));
        }
    }
}