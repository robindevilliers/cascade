package uk.co.malbec.welcometohell;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public static void selectOption(WebDriver webDriver, String selector, String value) {
        webDriver.findElements(By.cssSelector(selector))
                .stream()
                .filter(opt -> opt.getAttribute("value").equals(value))
                .findFirst()
                .ifPresent(WebElement::click);
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


    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
