package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.handler.WaitTenSeconds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static uk.co.malbec.cascade.conditions.Predicates.or;
import static uk.co.malbec.cascade.conditions.Predicates.withStep;

@Step(Portfolio.class)
public interface OpenAccountPage {

    //@StepPostHandler(WaitTenSeconds.class)
    public class OpenCurrentAccount implements OpenAccountPage {

        @OnlyRunWith
        Predicate predicate = or(
                withStep(Portfolio.CurrentAndSaverAccounts.class),
                withStep(Portfolio.CurrentAccountOnly.class)
        );

        @Demands
        private WebDriver webDriver;

        @Demands
        private List<Map> accounts;

        private Map<String, Object> currentAccount;

        @Given
        public void given() {

            for (Map account : accounts) {
                if ("Premium Current Account".equals(account.get("name"))) {
                    currentAccount = account;
                    break;
                }
            }

            currentAccount.put("transactions", new ArrayList<Map>() {{
                add(new HashMap<String, String>() {{
                    put("date", "30 Aug 2014");
                    put("description", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor");
                    put("type", "DEBIT");
                    put("amount", "4543");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "27 Aug 2014");
                    put("description", "In enim justo, rhoncus ut,");
                    put("type", "CREDIT");
                    put("amount", "3432");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "14 Aug 2014");
                    put("description", "Curabitur ullamcorper ultricies nisi.");
                    put("type", "DEBIT");
                    put("amount", "221");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "12 Aug 2014");
                    put("description", "Sed consequat, leo eget bibend");
                    put("type", "DEBIT");
                    put("amount", "4342");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "10 Aug 2014");
                    put("description", "Duis leo.");
                    put("type", "CREDIT");
                    put("amount", "5432");
                }});
            }});

        }

        @When
        public void when() {
            webDriver.findElement(By.cssSelector("[test-row-" + currentAccount.get("number") + "] [test-button-account-details]")).click();
        }

        @Then
        public void then(Throwable f) {

            assertEquals("Premium Current Account", webDriver.findElement(By.cssSelector("[test-field-account-name]")).getText());

            assertRow("0", "30 Aug 2014", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor", "4543", "", "400");
            assertRow("1", "27 Aug 2014", "In enim justo, rhoncus ut,", "", "3432", "4943");
            assertRow("2", "14 Aug 2014", "Curabitur ullamcorper ultricies nisi.", "221", "", "1511");
            assertRow("3", "12 Aug 2014", "Sed consequat, leo eget bibend", "4342", "", "1732");
            assertRow("4", "10 Aug 2014", "Duis leo.", "", "5432", "6074");


        }

        public void assertRow(String row, String date, String description, String debit, String credit, String balance) {
            assertEquals(date, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-date]", row))).getText());
            assertEquals(description, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-description]", row))).getText());
            assertEquals(debit, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-debit]", row))).getText());
            assertEquals(credit, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-credit]", row))).getText());
            assertEquals(balance, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-balance]", row))).getText());
        }
    }
}
