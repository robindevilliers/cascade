package uk.co.malbec.welcometohell.steps;

import com.sun.tools.javac.comp.Enter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(OpenGenderPage.class)
public interface EnterGender {

    class EnterMaleGender implements EnterGender {
        @Demands
        private WebDriver webDriver;

        @Supplies
        private String gender = "MALE";

        @When
        public void when() {
            selectOption(webDriver, "input", gender);

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Like Marmite", webDriver.getTitle());
        }
    }

    class EnterFemaleGender implements EnterGender {
        @Demands
        private WebDriver webDriver;

        @Supplies
        private String gender = "FEMALE";

        @When
        public void when() {
            selectOption(webDriver, "input", gender);

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Like Marmite", webDriver.getTitle());
        }
    }
}
