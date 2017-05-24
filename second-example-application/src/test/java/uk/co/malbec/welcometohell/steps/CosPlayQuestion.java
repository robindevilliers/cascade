package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.cascade.conditions.Predicates.or;
import static uk.co.malbec.cascade.conditions.Predicates.withStep;
import static uk.co.malbec.welcometohell.Utilities.selectOption;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step(Jurisdiction.Jedi.class)
public interface CosPlayQuestion {

    class YesToCosPlay implements CosPlayQuestion {
        @Demands
        private WebDriver webDriver;

        @OnlyRunWith
        Predicate predicate = withStep(EnterGender.EnterFemaleGender.class);

        @When
        public void when() {
            selectOption(webDriver, "input", "true");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Motoko Kusanagi", webDriver.getTitle());
        }
    }

    class NoToCosPlay implements CosPlayQuestion {
        @Demands
        private WebDriver webDriver;

        @OnlyRunWith
        Predicate predicate = withStep(EnterGender.EnterFemaleGender.class);

        @When
        public void when() {
            selectOption(webDriver, "input", "false");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration", webDriver.getTitle());
        }
    }
}
