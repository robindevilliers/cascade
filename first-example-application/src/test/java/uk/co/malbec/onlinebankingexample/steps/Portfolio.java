package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.onlinebankingexample.AccountsStateRendering;
import uk.co.malbec.onlinebankingexample.domain.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.co.malbec.onlinebankingexample.Utilities.assertElementIsNotPresent;
import static uk.co.malbec.onlinebankingexample.Utilities.assertTextEquals;

@Step({Challenge.class, Notice.class, BackToPorfolio.class})
public interface Portfolio {

    public class CurrentAccountOnly implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies(stateRenderer = AccountsStateRendering.class)
        private List<Account> accounts = new ArrayList<>();

        @Given
        public void given() {
            accounts.add(new Account("Premium Current Account", "Current", "1001", 40000));
        }

        @Then
        public void then() {
            assertTextEquals(webDriver, "[test-row-1001] [test-field-account-name]", "Premium Current Account");
            assertTextEquals(webDriver, "[test-row-1001] [test-field-account-balance]", "£ 400.00");
        }
    }

    public class CurrentAndSaverAccounts implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies(stateRenderer = AccountsStateRendering.class)
        private List<Account> accounts = new ArrayList<>();

        @Given
        public void given() {
            accounts.add(new Account("Premium Current Account", "Current", "1001", 40000));
            accounts.add(new Account("Easy Saver Account", "Saver", "1002", 10000));
        }

        @Then
        public void then() {
            assertTextEquals(webDriver, "[test-row-1001] [test-field-account-name]", "Premium Current Account");
            assertTextEquals(webDriver, "[test-row-1001] [test-field-account-balance]", "£ 400.00");
            assertTextEquals(webDriver, "[test-row-1002] [test-field-account-name]", "Easy Saver Account");
            assertTextEquals(webDriver, "[test-row-1002] [test-field-account-balance]", "£ 100.00");
        }
    }

    public class AllAccounts implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies(stateRenderer = AccountsStateRendering.class)
        private List<Account> accounts = new ArrayList<>();

        @Given
        public void given() {
            accounts.add(new Account("Premium Current Account", "Current", "1001", 40000));
            accounts.add(new Account("Easy Saver Account", "Saver", "1002", 10000));
            accounts.add(new Account("Fancy Mortgage", "Mortgage", "1004", -15498700));
        }

        @Then
        public void then() {
            assertTextEquals(webDriver, "[test-row-1001] [test-field-account-name]", "Premium Current Account");
            assertTextEquals(webDriver, "[test-row-1001] [test-field-account-balance]", "£ 400.00");
            assertTextEquals(webDriver, "[test-row-1002] [test-field-account-name]", "Easy Saver Account");
            assertTextEquals(webDriver, "[test-row-1002] [test-field-account-balance]", "£ 100.00");
            assertTextEquals(webDriver, "[test-row-1004] [test-field-account-name]", "Fancy Mortgage");
            assertTextEquals(webDriver, "[test-row-1004] [test-field-account-balance]", "£ (154,987.00)");
        }
    }

    public class MortgageAccountOnly implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies(stateRenderer = AccountsStateRendering.class)
        private List<Account> accounts = new ArrayList<>();

        @Given
        public void given() {
            accounts.add(new Account("Fancy Mortgage", "Mortgage", "1004", -15498700));
        }

        @Then
        public void then() {
            assertTextEquals(webDriver, "[test-row-1004] [test-field-account-name]", "Fancy Mortgage");
            assertTextEquals(webDriver, "[test-row-1004] [test-field-account-balance]", "£ (154,987.00)");
            assertElementIsNotPresent(webDriver, "[test-link-payments]");
        }
    }
}
