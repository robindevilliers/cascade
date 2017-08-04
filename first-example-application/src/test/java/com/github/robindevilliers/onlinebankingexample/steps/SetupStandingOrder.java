package com.github.robindevilliers.onlinebankingexample.steps;


import com.github.robindevilliers.cascade.annotations.*;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.onlinebankingexample.domain.Payment;
import com.github.robindevilliers.onlinebankingexample.domain.StandingOrder;

import java.util.List;

import static com.github.robindevilliers.onlinebankingexample.Utilities.*;

@SuppressWarnings("all")
@Step(OpenPaymentsPage.class)
public interface SetupStandingOrder {

    public class SetupStandingOrderForNow implements SetupStandingOrder {
        @Demands
        private WebDriver webDriver;

        @Demands
        public List<StandingOrder> standingOrders;

        @Demands
        public List<Payment> recentPayments;

        @When
        public void when() {
            enterText(webDriver, "[test-input-description]", "magazine subscription");
            enterText(webDriver, "[test-input-reference]", "rdevilliers");
            enterText(webDriver, "[test-input-account-number]", "98985656");
            enterText(webDriver, "[test-input-sort-code-one]", "11");
            enterText(webDriver, "[test-input-sort-code-two]", "22");
            enterText(webDriver, "[test-input-sort-code-three]", "33");
            click(webDriver, "[test-input-type-now]");
            select(webDriver, "[test-input-period]", "Monthly");
            enterText(webDriver, "[test-input-amount]", "12.00");
            click(webDriver, "[test-setup-cta]");
            waitForPage(webDriver);

            standingOrders.add(
                    new StandingOrder()
                            .setId("99")
                            .setDescription("magazine subscription")
                            .setReference("rdevilliers")
                            .setAccountNumber("98985656")
                            .setSortCode("112233")
                            .setDueDate(null)
                            .setPeriod("Monthly")
                            .setAmount("1200")
            );

            recentPayments.add(0,
                    new Payment()
                            .setDate(null)
                            .setDescription("magazine subscription")
                            .setReference("rdevilliers")
                            .setAccountNumber("98985656")
                            .setSortCode("112233")
                            .setCleared("")
                            .setAmount("1200")
            );

        }

        @Then
        public void then() {

            for (int row = 0; row < standingOrders.size(); row++) {
                assertStandingOrderRow(webDriver, row, standingOrders.get(row));
            }

            for (int row = 0; row < recentPayments.size(); row++) {
                assertRecentPaymentRow(webDriver, row, recentPayments.get(row));
            }
        }
    }

    public class SetupStandingOrderForLater implements SetupStandingOrder {
        @Demands
        private WebDriver webDriver;

        @Demands
        public List<StandingOrder> standingOrders;

        @Demands
        public List<Payment> recentPayments;

        @When
        public void when() {
            enterText(webDriver, "[test-input-description]", "magazine subscription");
            enterText(webDriver, "[test-input-reference]", "rdevilliers");
            enterText(webDriver, "[test-input-account-number]", "98985656");
            enterText(webDriver, "[test-input-sort-code-one]", "11");
            enterText(webDriver, "[test-input-sort-code-two]", "22");
            enterText(webDriver, "[test-input-sort-code-three]", "33");
            click(webDriver, "[test-input-type-specified]");
            enterText(webDriver, "[test-input-date-day]", "12");
            enterText(webDriver, "[test-input-date-month]", "30");
            enterText(webDriver, "[test-input-date-year]", "2014");
            select(webDriver, "[test-input-period]", "Monthly");
            enterText(webDriver, "[test-input-amount]", "12.00");
            click(webDriver, "[test-setup-cta]");
            waitForPage(webDriver);

            standingOrders.add(
                    new StandingOrder()
                            .setId("99")
                            .setDescription("magazine subscription")
                            .setReference("rdevilliers")
                            .setAccountNumber("98985656")
                            .setSortCode("112233")
                            .setDueDate(null)
                            .setPeriod("Monthly")
                            .setAmount("1200")
            );
        }

        @Then
        public void then() {

            for (int row = 0; row < standingOrders.size(); row++) {
                assertStandingOrderRow(webDriver, row, standingOrders.get(row));
            }

            for (int row = 0; row < recentPayments.size(); row++) {
                assertRecentPaymentRow(webDriver, row, recentPayments.get(row));
            }
        }
    }
}
