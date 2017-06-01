package com.github.robindevilliers.welcometohell.steps;


import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.annotations.Demands;
import com.github.robindevilliers.cascade.annotations.Narrative;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.annotations.Then;

import static org.junit.Assert.assertEquals;

@Step({MentallyIll.class, YouCanGo.class, EnterHowIsHell.class, DoneTime.class})
@Narrative("Present escaped conclusion.")
public class Escaped {

    @Demands
    private WebDriver webDriver;

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Escape", webDriver.getTitle());
    }
}
