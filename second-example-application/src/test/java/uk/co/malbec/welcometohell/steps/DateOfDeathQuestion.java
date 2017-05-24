package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.enterText;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step({SallyQuestion.NoToPinchingSally.class, DeclareSins.class})
@Narrative("Enter date of death and go to the payment page.")
public class DateOfDeathQuestion {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        enterText(webDriver, "input[type='date']", "01/01/2000");

        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Time to Pay", webDriver.getTitle());
    }
}
