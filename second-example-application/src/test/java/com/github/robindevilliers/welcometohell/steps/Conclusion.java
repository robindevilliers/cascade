package com.github.robindevilliers.welcometohell.steps;

import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.annotations.Demands;
import com.github.robindevilliers.cascade.annotations.Narrative;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.annotations.Then;

import static org.junit.Assert.assertEquals;

@Step({DirtyDog.class, VIPSection.class, MissedBooking.class})
@Narrative("Present the conclusion page.")
public class Conclusion {

    @Demands
    private WebDriver webDriver;

    @Then
    public void then() {
        assertEquals("Welcome to Hell | Conclusion", webDriver.getTitle());
    }
}
