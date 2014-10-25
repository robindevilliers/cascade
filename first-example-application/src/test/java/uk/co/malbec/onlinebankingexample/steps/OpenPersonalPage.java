package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@Step(Portfolio.class)
public class OpenPersonalPage {

    @Demands
    public WebDriver webDriver;

    @Supplies
    public Map<String, String> personalDetails;

    @Given
    public void given() {
        personalDetails = new HashMap<String, String>() {{
            put("name", "Robin de Villiers");
            put("nationality", "British");
            put("domicile", "UK");
            put("address", "7 Special Way, FairBank, ImaginaryVille, WOW007");
            put("mobile", "0788 1234 567");
            put("email", "robin@imaginaryville.co.uk");
        }};
    }

    @When
    public void when() {
        webDriver.findElement(By.cssSelector("[test-link-personal-details]")).click();
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
    }

    @Then
    public void then(Throwable f) {
        assertNull(f);

        assertEquals("Robin de Villiers", webDriver.findElement(By.cssSelector("[test-field-name]")).getText());
        assertEquals("British", webDriver.findElement(By.cssSelector("[test-field-nationality]")).getText());
        assertEquals("UK", webDriver.findElement(By.cssSelector("[test-field-domicile]")).getText());
        assertEquals("7 Special Way, FairBank, ImaginaryVille, WOW007", webDriver.findElement(By.cssSelector("[test-field-address]")).getText());
        assertEquals("0788 1234 567", webDriver.findElement(By.cssSelector("[test-field-mobile]")).getText());
        assertEquals("robin@imaginaryville.co.uk", webDriver.findElement(By.cssSelector("[test-field-email]")).getText());
    }

}
