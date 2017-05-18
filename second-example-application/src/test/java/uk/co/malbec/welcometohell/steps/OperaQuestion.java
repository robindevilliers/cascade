package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(MarmiteQuestion.HatesMarmite.class)
public interface OperaQuestion {


    class BeenToTheOpera implements OperaQuestion {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            selectOption(webDriver, "input", "true");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Done Time", webDriver.getTitle());
        }
    }

    class HaveNotBeenToTheOpera implements OperaQuestion {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            webDriver.findElements(By.cssSelector("input"))
                    .stream()
                    .filter(w -> w.getAttribute("value").equals("false"))
                    .findFirst()
                    .ifPresent(WebElement::click);

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Like Ballet", webDriver.getTitle());
        }
    }
}
