package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            currentAccount.put("number","12345678");
            currentAccount.put("balance", 400);
            accounts.add(currentAccount);
        }

        @Then
        public void then(Throwable f){
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-12345678] [test-field-account-name]")).getText(), "Premium Current Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-12345678] [test-field-account-balance]")).getText(), "400");
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
            currentAccount.put("number","12345678");
            currentAccount.put("balance", 400);
            accounts.add(currentAccount);

            Map<String, Object> saverAccount = new HashMap<String, Object>();
            saverAccount.put("name","Easy Saver Account");
            saverAccount.put("number","12345679");
            saverAccount.put("balance", 100);
            accounts.add(saverAccount);
        }

        @Then
        public void then(Throwable f){

            assertEquals(webDriver.findElement(By.cssSelector("[test-row-12345678] [test-field-account-name]")).getText(), "Premium Current Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-12345678] [test-field-account-balance]")).getText(), "400");

            assertEquals(webDriver.findElement(By.cssSelector("[test-row-12345679] [test-field-account-name]")).getText(), "Easy Saver Account");
            assertEquals(webDriver.findElement(By.cssSelector("[test-row-12345679] [test-field-account-balance]")).getText(), "100");

        }
    }
}
