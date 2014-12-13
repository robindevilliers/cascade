package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import java.util.List;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenPaymentsPage.class)
@ReEntrantTerminator(1)
public interface SetupStandingOrder {

    public class SetupStandingOrderForNow implements SetupStandingOrder {
        @Demands
        private WebDriver webDriver;

        @Demands
        public List<String[]> expectedStandingOrders;

        @Demands
        public List<String[]> expectedRecentPayments;

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

            expectedStandingOrders.add(new String[]{"magazine subscription", "rdevilliers", "98985656", "112233", null, "Monthly", "£ 12.00"});

            expectedRecentPayments.add(0, new String[]{null, "magazine subscription", "rdevilliers", "98985656", "112233", "", "£ 12.00"});
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            int i = 0;
            for (String[] expected : expectedStandingOrders) {
                assertStandingOrderRow(webDriver, "" + i, expected[0], expected[1], expected[2], expected[3], expected[4], expected[5], expected[6]);
                i++;
            }

            i = 0;
            for (String[] expected : expectedRecentPayments) {
                assertRecentPaymentRow(webDriver, "" + i, expected[0], expected[1], expected[2], expected[3], expected[4], expected[5], expected[6]);
                i++;
            }
        }
    }

    public class SetupStandingOrderForLater implements SetupStandingOrder {
        @Demands
        private WebDriver webDriver;

        @Demands
        public List<String[]> expectedStandingOrders;

        @Demands
        public List<String[]> expectedRecentPayments;

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

            expectedStandingOrders.add(new String[]{"magazine subscription", "rdevilliers", "98985656", "112233", null, "Monthly", "£ 12.00"});
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);

            int i = 0;
            for (String[] expected : expectedStandingOrders) {
                assertStandingOrderRow(webDriver, "" + i, expected[0], expected[1], expected[2], expected[3], expected[4], expected[5], expected[6]);
                i++;
            }

            i = 0;
            for (String[] expected : expectedRecentPayments) {
                assertRecentPaymentRow(webDriver, "" + i, expected[0], expected[1], expected[2], expected[3], expected[4], expected[5], expected[6]);
                i++;
            }
        }
    }
}
