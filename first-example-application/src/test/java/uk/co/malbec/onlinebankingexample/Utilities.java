package uk.co.malbec.onlinebankingexample;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.String.format;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class Utilities {

    public static void enterText(WebDriver webDriver, String selector, String text) {

        //there is a issue with the current selenium webdriver incorrectly sending keys to the address bar, rather than the actual input field.
        for (int i = 0; i < 3; i++) {
            webDriver.findElement(By.cssSelector(selector)).sendKeys(text);
            if (webDriver.findElement(By.cssSelector(selector)).getAttribute("value").contains(text)) {
                break;
            }
        }
    }

    public static void select(WebDriver webDriver, String selector, String value) {
        new Select(webDriver.findElement(By.cssSelector(selector))).selectByValue(value);
    }

    public static void click(WebDriver webDriver, String selector) {
        webDriver.findElement(By.cssSelector(selector)).click();
    }

    public static void waitForPage(WebDriver webDriver) {
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
    }

    public static String readText(WebDriver webDriver, String selector) {
        return webDriver.findElement(By.cssSelector(selector)).getText();
    }

    public static void assertTextEquals(WebDriver webDriver, String selector, String text) {
        assertEquals(text, webDriver.findElement(By.cssSelector(selector)).getText());
    }

    public static void assertElementPresent(WebDriver webDriver, String selector) {
        assertEquals(1, webDriver.findElements(By.cssSelector(selector)).size());
    }

    public static void assertElementDisplayed(WebDriver webDriver, String selector) {
        assertTrue(webDriver.findElement(By.cssSelector(selector)).isDisplayed());
    }

    public static void assertElementIsNotPresent(WebDriver webDriver, String selector) {
        assertEquals(0, webDriver.findElements(By.cssSelector(selector)).size());
    }

    public static void assertAccountListRow(WebDriver webDriver, String row, String date, String description, String debit, String credit, String balance) {
        assertEquals(date, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-date]", row))).getText());
        assertEquals(description, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-description]", row))).getText());
        assertEquals(debit, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-debit]", row))).getText());
        assertEquals(credit, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-credit]", row))).getText());
        assertEquals(balance, webDriver.findElement(By.cssSelector(format("[test-row-%s] [test-field-balance]", row))).getText());
    }

    public static void assertStandingOrderRow(WebDriver webDriver, String row, String description, String reference, String accountNumber, String sortCode, String lastDate, String period, String amount) {
        assertEquals(description, webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-description]", row))).getText());
        assertEquals(reference, webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-reference]", row))).getText());
        assertEquals(accountNumber, webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-account-number]", row))).getText());
        assertEquals(sortCode, webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-sort-code]", row))).getText());

        //TODO -fix when we add time control to server
        if (lastDate != null) {
            assertEquals(lastDate, webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-last-date]", row))).getText());
        }

        assertEquals(period, webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-period]", row))).getText());
        assertEquals(amount, webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-amount]", row))).getText());
    }

    public static void assertRecentPaymentRow(WebDriver webDriver, String row, String date, String description, String reference, String accountNumber, String sortCode, String cleared, String amount) {
        if (date != null) {
            assertEquals(date, webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-date]", row))).getText());
        }
        assertEquals(description, webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-description]", row))).getText());
        assertEquals(reference, webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-reference]", row))).getText());
        assertEquals(accountNumber, webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-account-number]", row))).getText());
        assertEquals(sortCode, webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-sort-code]", row))).getText());
        assertEquals(cleared, webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-cleared]", row))).getText());
        assertEquals(amount, webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-amount]", row))).getText());
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
