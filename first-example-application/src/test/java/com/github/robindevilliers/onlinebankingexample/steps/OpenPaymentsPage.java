package com.github.robindevilliers.onlinebankingexample.steps;


import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.onlinebankingexample.RecentPaymentsStateRendering;
import com.github.robindevilliers.onlinebankingexample.StandingOrdersStateRendering;
import com.github.robindevilliers.onlinebankingexample.Utilities;
import com.github.robindevilliers.onlinebankingexample.domain.Payment;
import com.github.robindevilliers.onlinebankingexample.domain.StandingOrder;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.conditions.Predicate;
import com.github.robindevilliers.onlinebankingexample.StandingOrdersTransitionRendering;

import java.util.ArrayList;
import java.util.List;

import static com.github.robindevilliers.cascade.conditions.Predicates.or;
import static com.github.robindevilliers.cascade.conditions.Predicates.withStep;

@SuppressWarnings("all")
@Step({Portfolio.class, CancelStandingOrder.class})
public class OpenPaymentsPage {

    @OnlyRunWith
    Predicate predicate = or(
            withStep(Portfolio.CurrentAccountOnly.class),
            withStep(Portfolio.CurrentAndSaverAccounts.class),
            withStep(Portfolio.AllAccounts.class)
    );

    @Demands
    public WebDriver webDriver;

    @Supplies(stateRenderer = StandingOrdersStateRendering.class, transitionRenderer = StandingOrdersTransitionRendering.class)
    public List<StandingOrder> standingOrders = new ArrayList<>();

    @Supplies(stateRenderer = RecentPaymentsStateRendering.class)
    public List<Payment> recentPayments = new ArrayList<>();

    @Given
    public void given() {
        standingOrders.add(
                new StandingOrder()
                .setId("1")
                .setDueDate("30 Aug 2014")
                .setDescription("Lorem ipsum dolor sit amet.")
                .setReference("10001-02203-222")
                .setPeriod("Monthly")
                .setAmount("2343")
                .setAccountNumber("93841732")
                .setSortCode("893810")
        );

        standingOrders.add(
          new StandingOrder()
                  .setId("2")
                  .setDueDate("30 Sep 2014")
                  .setDescription("Sed consequat")
                  .setReference("X12345-ROBIN-DE-VILLIERS")
                  .setPeriod("Quarterly")
                  .setAmount("1500")
                  .setAccountNumber("44433232")
                  .setSortCode("893810")
        );

        standingOrders.add(
                new StandingOrder()
                        .setId("3")
                        .setDueDate("02 Dec 2014")
                        .setDescription("Etiam sit amet")
                        .setReference("11102929920293837X")
                        .setPeriod("Yearly")
                        .setAmount("8500")
                        .setAccountNumber("23334332")
                        .setSortCode("100210")
        );

        recentPayments.add(
                new Payment()
                        .setDate("30 Aug 2014")
                        .setDescription("Lorem ipsum dolor sit amet.")
                        .setReference("1112")
                        .setAccountNumber("93841732")
                        .setSortCode("893810")
                        .setAmount("2343")
                        .setCleared("")
        );

        recentPayments.add(
                new Payment()
                        .setDate("29 Aug 2014")
                        .setDescription("Aenean imperdiet")
                        .setReference("111223")
                        .setAccountNumber("12345678")
                        .setSortCode("100102")
                        .setAmount("20000")
                        .setCleared("")
        );

        recentPayments.add(
                new Payment()
                        .setDate("30 May 2014")
                        .setDescription("Sed consequat")
                        .setReference("1113")
                        .setAccountNumber("44433232")
                        .setSortCode("893810")
                        .setAmount("1500")
                        .setCleared("02 Jun 2014")
        );

        recentPayments.add(
                new Payment()
                        .setDate("02 Dec 2014")
                        .setDescription("Etiam sit amet")
                        .setReference("1114")
                        .setAccountNumber("23334332")
                        .setSortCode("100210")
                        .setAmount("8500")
                        .setCleared("04 Dec 2014")
        );
    }

    @When
    public void when() {
        Utilities.click(webDriver, "[test-link-payments]");
        Utilities.waitForPage(webDriver);
    }

    @Then
    public void then() {

        for (int row = 0; row < standingOrders.size(); row++) {
            Utilities.assertStandingOrderRow(webDriver, row, standingOrders.get(row));
        }

        for (int row = 0; row < recentPayments.size(); row++){
            Utilities.assertRecentPaymentRow(webDriver, row, recentPayments.get(row));
        }
    }
}
