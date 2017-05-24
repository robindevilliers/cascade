package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(MarmiteQuestion.HatesMarmite.class)
@Narrative("Opera question.")
public interface OperaQuestion {

    @Narrative("Enter been to the opera and go to the done time page.")
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

    @Narrative("Enter has not been to the opera and go to the ballet page.")
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
