package uk.co.malbec.onlinebankingexample.steps;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@Step
public class OpenLandingPage {

    @Supplies
    private WebDriver webDriver;

    @Given
    public void given() {
        webDriver = new FirefoxDriver();

    }

    @When
    public void when() {
        webDriver.get("http://localhost:8080");
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
    }

    @Then
    //TODO - when throwable argument is not suppied, then there must be a helpful error message
    //TODO - if throwable not supplied here, then we don't expect an exception??  likewise when there is no then clause
    public void then(Throwable f) {
        assertNull(f);
        assertEquals("Fiery Horse Banking", webDriver.getTitle());
        assertEquals(1, webDriver.findElements(By.cssSelector("[test-form-login]")).size());

    }

    @Clear
    public void clear(){
        webDriver.close();
    }

}
