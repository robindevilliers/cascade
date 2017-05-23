package uk.co.malbec.welcometohell.steps;


import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;

import static org.junit.Assert.assertEquals;

@Step({MentallyIll.class, YouCanGo.class, EnterHowIsHell.class, AlreadyDoneTime.class, DoneTime.class})
public class Escaped {

    @Demands
    private WebDriver webDriver;

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Escape", webDriver.getTitle());
    }
}
