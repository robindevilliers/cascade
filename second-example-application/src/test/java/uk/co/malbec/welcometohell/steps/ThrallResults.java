package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(ThrallQuestion.YesToThrall.class)
@Narrative("Proceed and go to the booking page.")
public class ThrallResults {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Booking", webDriver.getTitle());
    }
}
