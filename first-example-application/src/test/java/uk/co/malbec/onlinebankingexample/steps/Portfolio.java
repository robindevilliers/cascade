package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@Step({Challenge.class, Notice.class})
public interface Portfolio {


    public class CurrentAccountOnly implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies
        private List<Map> accounts = new ArrayList<Map>();

        @Given
        public void given(){
            Map<String, Object> currentAccount = new HashMap<String, Object>();
            currentAccount.put("name","Premium Current Account");
            currentAccount.put("type", "Current");
            currentAccount.put("number","1001");
            currentAccount.put("balance", 40000);
            accounts.add(currentAccount);
        }

        @Then
        public void then(Throwable f){
            assertNull(f);
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1001] [test-field-account-name]")).getText(), "Premium Current Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1001] [test-field-account-balance]")).getText(), "£ 400.00");
        }
    }

    public class CurrentAndSaverAccounts implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies
        private List<Map> accounts = new ArrayList<Map>();

        @Given
        public void given(){
            Map<String, Object> currentAccount = new HashMap<String, Object>();
            currentAccount.put("name","Premium Current Account");
            currentAccount.put("type", "Current");
            currentAccount.put("number","1001");
            currentAccount.put("balance", 40000);
            accounts.add(currentAccount);

            Map<String, Object> saverAccount = new HashMap<String, Object>();
            saverAccount.put("name","Easy Saver Account");
            saverAccount.put("type", "Saver");
            saverAccount.put("number","1002");
            saverAccount.put("balance", 10000);
            accounts.add(saverAccount);
        }

        @Then
        public void then(Throwable f){
            assertNull(f);

            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1001] [test-field-account-name]")).getText(), "Premium Current Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1001] [test-field-account-balance]")).getText(), "£ 400.00");

            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1002] [test-field-account-name]")).getText(), "Easy Saver Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1002] [test-field-account-balance]")).getText(), "£ 100.00");

        }
    }

    public class AllAccounts implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies
        private List<Map> accounts = new ArrayList<Map>();

        @Given
        public void given(){
            Map<String, Object> currentAccount = new HashMap<String, Object>();
            currentAccount.put("name","Premium Current Account");
            currentAccount.put("type", "Current");
            currentAccount.put("number","1001");
            currentAccount.put("balance", 40000);
            accounts.add(currentAccount);

            Map<String, Object> saverAccount = new HashMap<String, Object>();
            saverAccount.put("name","Easy Saver Account");
            saverAccount.put("type", "Saver");
            saverAccount.put("number","1002");
            saverAccount.put("balance", 10000);
            accounts.add(saverAccount);

            Map<String, Object> mortgageAccount = new HashMap<String, Object>();
            mortgageAccount.put("name","Fancy Mortgage");
            mortgageAccount.put("type", "Mortgage");
            mortgageAccount.put("number","1004");
            mortgageAccount.put("balance", -15498700);
            accounts.add(mortgageAccount);
        }

        @Then
        public void then(Throwable f){
            assertNull(f);

            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1001] [test-field-account-name]")).getText(), "Premium Current Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1001] [test-field-account-balance]")).getText(), "£ 400.00");

            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1002] [test-field-account-name]")).getText(), "Easy Saver Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1002] [test-field-account-balance]")).getText(), "£ 100.00");

            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1004] [test-field-account-name]")).getText(), "Fancy Mortgage");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1004] [test-field-account-balance]")).getText(), "£ (154,987.00)");
        }
    }

    public class MortgageAccountOnly implements Portfolio {

        @Demands
        private WebDriver webDriver;

        @Supplies
        private List<Map> accounts = new ArrayList<Map>();

        @Given
        public void given(){
            Map<String, Object> mortgageAccount = new HashMap<String, Object>();
            mortgageAccount.put("name","Fancy Mortgage");
            mortgageAccount.put("type", "Mortgage");
            mortgageAccount.put("number","1004");
            mortgageAccount.put("balance", -15498700);
            accounts.add(mortgageAccount);
        }

        @Then
        public void then(Throwable f){
            assertNull(f);
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1004] [test-field-account-name]")).getText(), "Fancy Mortgage");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-1004] [test-field-account-balance]")).getText(), "£ (154,987.00)");
        }
    }
}
