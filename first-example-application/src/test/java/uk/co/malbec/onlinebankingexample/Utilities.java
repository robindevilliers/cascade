package uk.co.malbec.onlinebankingexample;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.onlinebankingexample.domain.Payment;
import uk.co.malbec.onlinebankingexample.domain.StandingOrder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void assertStandingOrderRow(WebDriver webDriver, int row, StandingOrder standingOrder) {
        assertEquals(standingOrder.getDescription(), webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-description]", row))).getText());
        assertEquals(standingOrder.getReference(), webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-reference]", row))).getText());
        assertEquals(standingOrder.getAccountNumber(), webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-account-number]", row))).getText());
        assertEquals(standingOrder.getSortCode(), webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-sort-code]", row))).getText());

        if (standingOrder.getDueDate() != null) {
            assertEquals(standingOrder.getDueDate(), webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-last-date]", row))).getText());
        }

        assertEquals(standingOrder.getPeriod(), webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-period]", row))).getText());
        assertEquals(standingOrder.getAmount(), parseAmount(webDriver.findElement(By.cssSelector(format("[test-standing-order-row-%s] [test-field-amount]", row))).getText()));
    }

    public static void assertRecentPaymentRow(WebDriver webDriver, int row, Payment payment) {
        //TODO - we don't assert the date if the expected date is null - this is because we don't have a time machine in the app at the moment.
        if (payment.getDate() != null) {
            assertEquals(payment.getDate(), webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-date]", row))).getText());
        }
        assertEquals(payment.getDescription(), webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-description]", row))).getText());
        assertEquals(payment.getReference(), webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-reference]", row))).getText());
        assertEquals(payment.getAccountNumber(), webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-account-number]", row))).getText());
        assertEquals(payment.getSortCode(), webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-sort-code]", row))).getText());
        assertEquals(payment.getCleared(), webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-cleared]", row))).getText());
        assertEquals(payment.getAmount(), parseAmount(webDriver.findElement(By.cssSelector(format("[test-recent-payment-row-%s] [test-field-amount]", row))).getText()));
    }

    public static String parseAmount(String amountString) {
        Pattern amountPattern = Pattern.compile("£\\s*(\\d*).(\\d*)");

        Matcher matcher = amountPattern.matcher(amountString);
        if (matcher.matches()) {
            String pounds = matcher.group(1);
            String pence = matcher.group(2);
            return pounds + pence;
        } else {
            return "";
        }
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
