package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static uk.co.malbec.cascade.conditions.Predicates.or;
import static uk.co.malbec.cascade.conditions.Predicates.withStep;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(Portfolio.class)
public interface OpenAccountPage {

    public class OpenCurrentAccount implements OpenAccountPage {
        @OnlyRunWith
        Predicate predicate = or(
                withStep(Portfolio.CurrentAndSaverAccounts.class),
                withStep(Portfolio.CurrentAccountOnly.class),
                withStep(Portfolio.AllAccounts.class)
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
            click(webDriver, String.format("[test-row-%s] [test-button-account-details]", currentAccount.get("number")));
            waitForPage(webDriver);
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);

            assertTextEquals(webDriver, "[test-field-account-name]", "Premium Current Account");

            assertAccountListRow(webDriver, "0", "30 Aug 2014", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor", "£ 45.43", "", "£ 400.00");
            assertAccountListRow(webDriver, "1", "27 Aug 2014", "In enim justo, rhoncus ut,", "", "£ 34.32", "£ 445.43");
            assertAccountListRow(webDriver, "2", "14 Aug 2014", "Curabitur ullamcorper ultricies nisi.", "£ 2.21", "", "£ 411.11");
            assertAccountListRow(webDriver, "3", "12 Aug 2014", "Sed consequat, leo eget bibend", "£ 43.42", "", "£ 413.32");
            assertAccountListRow(webDriver, "4", "10 Aug 2014", "Duis leo.", "", "£ 54.32", "£ 456.74");
        }
    }

    public class OpenSaverAccount implements OpenAccountPage {

        @OnlyRunWith
        Predicate predicate = or(
                withStep(Portfolio.CurrentAndSaverAccounts.class),
                withStep(Portfolio.AllAccounts.class)
        );

        @Demands
        private WebDriver webDriver;

        @Demands
        private List<Map> accounts;

        private Map<String, Object> currentAccount;

        @Given
        public void given() {

            for (Map account : accounts) {
                if ("Easy Saver Account".equals(account.get("name"))) {
                    currentAccount = account;
                    break;
                }
            }

            currentAccount.put("transactions", new ArrayList<Map>() {{
                add(new HashMap<String, String>() {{
                    put("date", "30 Aug 2014");
                    put("description", "Nulla consequat massa quis enim");
                    put("type", "DEBIT");
                    put("amount", "1500");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "27 Aug 2014");
                    put("description", "Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu");
                    put("type", "CREDIT");
                    put("amount", "1500");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "14 Aug 2014");
                    put("description", "In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo");
                    put("type", "DEBIT");
                    put("amount", "3000");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "12 Aug 2014");
                    put("description", "Nullam dictum felis eu pede mollis pretium");
                    put("type", "DEBIT");
                    put("amount", "2000");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "10 Aug 2014");
                    put("description", "Integer tincidunt");
                    put("type", "CREDIT");
                    put("amount", "1000");
                }});
            }});
        }

        @When
        public void when() {
            click(webDriver, format("[test-row-%s] [test-button-account-details]", currentAccount.get("number")));
            waitForPage(webDriver);
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            assertTextEquals(webDriver, "[test-field-account-name]", "Easy Saver Account");

            assertAccountListRow(webDriver, "0", "30 Aug 2014", "Nulla consequat massa quis enim", "£ 15.00", "", "£ 100.00");
            assertAccountListRow(webDriver, "1", "27 Aug 2014", "Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu", "", "£ 15.00", "£ 115.00");
            assertAccountListRow(webDriver, "2", "14 Aug 2014", "In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo", "£ 30.00", "", "£ 100.00");
            assertAccountListRow(webDriver, "3", "12 Aug 2014", "Nullam dictum felis eu pede mollis pretium", "£ 20.00", "", "£ 130.00");
            assertAccountListRow(webDriver, "4", "10 Aug 2014", "Integer tincidunt", "", "£ 10.00", "£ 150.00");
        }
    }

    public class OpenMortgageAccount implements OpenAccountPage {

        @OnlyRunWith
        Predicate predicate = or(
                withStep(Portfolio.MortgageAccountOnly.class),
                withStep(Portfolio.AllAccounts.class)
        );

        @Demands
        private WebDriver webDriver;

        @Demands
        private List<Map> accounts;

        private Map<String, Object> currentAccount;

        @Given
        public void given() {

            for (Map account : accounts) {
                if ("Fancy Mortgage".equals(account.get("name"))) {
                    currentAccount = account;
                    break;
                }
            }

            currentAccount.put("transactions", new ArrayList<Map>() {{
                add(new HashMap<String, String>() {{
                    put("date", "30 Aug 2014");
                    put("description", "Nulla consequat massa quis enim");
                    put("type", "DEBIT");
                    put("amount", "1500");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "27 Aug 2014");
                    put("description", "Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu");
                    put("type", "CREDIT");
                    put("amount", "1500");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "14 Aug 2014");
                    put("description", "In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo");
                    put("type", "DEBIT");
                    put("amount", "3000");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "12 Aug 2014");
                    put("description", "Nullam dictum felis eu pede mollis pretium");
                    put("type", "DEBIT");
                    put("amount", "2000");
                }});
                add(new HashMap<String, String>() {{
                    put("date", "10 Aug 2014");
                    put("description", "Integer tincidunt");
                    put("type", "CREDIT");
                    put("amount", "1000");
                }});
            }});
        }

        @When
        public void when() {
            click(webDriver, format("[test-row-%s] [test-button-account-details]", currentAccount.get("number")));
            waitForPage(webDriver);
        }

        @Then
        public void then(Throwable f) {
            assertNull(f);
            assertEquals("Fancy Mortgage", webDriver.findElement(By.cssSelector("[test-field-account-name]")).getText());

            assertAccountListRow(webDriver, "0", "30 Aug 2014", "Nulla consequat massa quis enim", "£ 15.00", "", "£ (154,987.00)");
            assertAccountListRow(webDriver, "1", "27 Aug 2014", "Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu", "", "£ 15.00", "£ (154,972.00)");
            assertAccountListRow(webDriver, "2", "14 Aug 2014", "In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo", "£ 30.00", "", "£ (154,987.00)");
            assertAccountListRow(webDriver, "3", "12 Aug 2014", "Nullam dictum felis eu pede mollis pretium", "£ 20.00", "", "£ (154,957.00)");
            assertAccountListRow(webDriver, "4", "10 Aug 2014", "Integer tincidunt", "", "£ 10.00", "£ (154,937.00)");
        }
    }
}
