package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.enterText;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step({EnterPlea.Guilty.class, EnterPleaAgain.GuiltyFinally.class, SallyQuestion.YesToPinchingSally.class})
@Narrative("Enter sins and go to the date of death page.")
public class DeclareSins {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        enterText(webDriver, "input[name='sin-1']", "I like barbarella.");
        enterText(webDriver, "input[name='sin-2']", "I had chocolate 2 months in a row.");

        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Date of Death", webDriver.getTitle());
    }
}
