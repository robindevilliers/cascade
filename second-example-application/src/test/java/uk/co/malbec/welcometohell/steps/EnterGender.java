package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(OpenGenderPage.class)
@Narrative("Gender question.")
public interface EnterGender {

    @Narrative("Enter male gender and go to marmite page.")
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

    @Narrative("Enter female gender and go to marmite page.")
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
