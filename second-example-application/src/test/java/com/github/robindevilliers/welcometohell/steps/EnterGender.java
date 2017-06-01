package com.github.robindevilliers.welcometohell.steps;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.welcometohell.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@Step(OpenGenderPage.class)
@Narrative("Gender question.")
public interface EnterGender {

    @Narrative("Enter male gender and go to marmalade page.")
    class EnterMaleGender implements EnterGender {
        @Demands
        private WebDriver webDriver;

        @Supplies
        private String gender = "MALE";

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", gender);

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Like Marmalade", webDriver.getTitle());
        }
    }

    @Narrative("Enter female gender and go to marmalade page.")
    class EnterFemaleGender implements EnterGender {
        @Demands
        private WebDriver webDriver;

        @Supplies
        private String gender = "FEMALE";

        @When
        public void when() {
            Utilities.selectOption(webDriver, "input", gender);

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            Utilities.waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Like Marmalade", webDriver.getTitle());
        }
    }
}
