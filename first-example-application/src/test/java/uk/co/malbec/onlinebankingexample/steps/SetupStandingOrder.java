package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPaymentsPage.class)
public interface SetupStandingOrder {

    public class SetupStandingOrderForNow implements SetupStandingOrder {
        @Demands
        private WebDriver webDriver;

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
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            assertStandingOrderRow(webDriver, "0", "Lorem ipsum dolor sit amet.", "10001-02203-222", "93841732", "893810", "30 Aug 2014", "Monthly", "£ 23.43");
            assertStandingOrderRow(webDriver, "1", "Sed consequat", "X12345-ROBIN-DE-VILLIERS", "44433232", "893810", "30 Sep 2014", "Quarterly", "£ 15.00");
            assertStandingOrderRow(webDriver, "2", "Etiam sit amet", "11102929920293837X", "23334332", "100210", "02 Dec 2014", "Yearly", "£ 85.00");
            assertStandingOrderRow(webDriver, "3", "magazine subscription", "rdevilliers", "98985656", "112233", null, "Monthly", "£ 12.00");

            assertRecentPaymentRow(webDriver, "0", null, "magazine subscription", "rdevilliers", "98985656", "112233", "", "£ 12.00");
            assertRecentPaymentRow(webDriver, "1", "30 Aug 2014", "Lorem ipsum dolor sit amet.", "1112", "93841732", "893810", "", "£ 23.43");
            assertRecentPaymentRow(webDriver, "2", "29 Aug 2014", "Aenean imperdiet", "111223", "12345678", "100102", "", "£ 200.00");
            assertRecentPaymentRow(webDriver, "3", "30 May 2014", "Sed consequat", "1113", "44433232", "893810", "02 Jun 2014", "£ 15.00");
            assertRecentPaymentRow(webDriver, "4", "02 Dec 2014", "Etiam sit amet", "1114", "23334332", "100210", "04 Dec 2014", "£ 85.00");
        }
    }

    public class SetupStandingOrderForLater implements SetupStandingOrder {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            enterText(webDriver, "[test-input-description]", "magazine subscription");
            enterText(webDriver, "[test-input-reference]", "rdevilliers");
            enterText(webDriver, "[test-input-account-number]", "98985656");
            enterText(webDriver, "[test-input-sort-code-one]", "11");
            enterText(webDriver, "[test-input-sort-code-two]", "22");
            enterText(webDriver, "[test-input-sort-code-three]", "33");
            click(webDriver, "[test-input-type-specified]");
            enterText(webDriver, "[test-input-date]", "12/30/14");
            select(webDriver, "[test-input-period]", "Monthly");
            enterText(webDriver, "[test-input-amount]", "12.00");
            click(webDriver, "[test-setup-cta]");
            waitForPage(webDriver);
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            assertStandingOrderRow(webDriver, "0", "Lorem ipsum dolor sit amet.", "10001-02203-222", "93841732", "893810", "30 Aug 2014", "Monthly", "£ 23.43");
            assertStandingOrderRow(webDriver, "1", "Sed consequat", "X12345-ROBIN-DE-VILLIERS", "44433232", "893810", "30 Sep 2014", "Quarterly", "£ 15.00");
            assertStandingOrderRow(webDriver, "2", "Etiam sit amet", "11102929920293837X", "23334332", "100210", "02 Dec 2014", "Yearly", "£ 85.00");
            assertStandingOrderRow(webDriver, "3", "magazine subscription", "rdevilliers", "98985656", "112233", null, "Monthly", "£ 12.00");


            assertRecentPaymentRow(webDriver, "0", "30 Aug 2014", "Lorem ipsum dolor sit amet.", "1112", "93841732", "893810", "", "£ 23.43");
            assertRecentPaymentRow(webDriver, "1", "29 Aug 2014", "Aenean imperdiet", "111223", "12345678", "100102", "", "£ 200.00");
            assertRecentPaymentRow(webDriver, "2", "30 May 2014", "Sed consequat", "1113", "44433232", "893810", "02 Jun 2014", "£ 15.00");
            assertRecentPaymentRow(webDriver, "3", "02 Dec 2014", "Etiam sit amet", "1114", "23334332", "100210", "04 Dec 2014", "£ 85.00");
        }
    }
}
