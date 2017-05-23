package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step({DirtyDog.class, VIPSection.class, MissedBooking.class})
public class Conclusion {

    @Demands
    private WebDriver webDriver;

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Conclusion", webDriver.getTitle());
    }
}
