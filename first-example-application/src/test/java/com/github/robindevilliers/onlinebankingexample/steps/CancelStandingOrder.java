package com.github.robindevilliers.onlinebankingexample.steps;


import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.onlinebankingexample.domain.StandingOrder;

import java.util.List;

import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

@SuppressWarnings("all")
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