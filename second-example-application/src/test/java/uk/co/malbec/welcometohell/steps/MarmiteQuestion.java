package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.malbec.cascade.annotations.*;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(EnterGender.class)
@Narrative("Marmite question.")
public interface MarmiteQuestion {

    @Narrative("Enter likes marmite and go to mentally ill page.")
    class LikesMarmite implements MarmiteQuestion {
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
            assertEquals("Welcome to Hell | Crazy", webDriver.getTitle());
        }
    }

    @Narrative("Enter hates marmite and go to opera page.")
    class HatesMarmite implements MarmiteQuestion {
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
            assertEquals("Welcome to Hell | Been to Opera", webDriver.getTitle());
        }
    }
}
