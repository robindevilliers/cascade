package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(OperaQuestion.HaveNotBeenToTheOpera.class)
@Narrative("Ballet question.")
public interface BalletQuestion {

    @Narrative("Enter likes ballet and go to the dirty dog page.")
    class LikesBallet implements BalletQuestion {
        @Demands
        private WebDriver webDriver;

        @When
        public void when() {
            webDriver.findElements(By.cssSelector("input"))
                    .stream()
                    .filter(w -> w.getAttribute("value").equals("true"))
                    .findFirst()
                    .ifPresent(WebElement::click);

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Good Bye", webDriver.getTitle());
        }
    }

    @Narrative("Enter hates ballet and go to the jurisdiction page.")
    class HatesBallet implements BalletQuestion {
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
            assertEquals("Welcome to Hell | Jurisdiction", webDriver.getTitle());
        }
    }
}
