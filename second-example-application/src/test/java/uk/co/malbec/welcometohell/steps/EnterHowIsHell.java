package uk.co.malbec.welcometohell.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.enterText;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step({Jurisdiction.Hindu.class, Jurisdiction.Buddhist.class})
@Narrative("Enter how is hell level and go to the escaped page.")
public class EnterHowIsHell {

    @Demands
    private WebDriver webDriver;

    @When
    public void when() {
        enterText(webDriver, "input[type='number']", "-15");

        webDriver.findElement(By.cssSelector("button[type=submit]")).click();
        waitForPage(webDriver);
    }

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Escape", webDriver.getTitle());
    }
}
