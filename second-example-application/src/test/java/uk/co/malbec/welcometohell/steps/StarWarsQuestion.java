package uk.co.malbec.welcometohell.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.cascade.conditions.Predicates.*;
import static uk.co.malbec.welcometohell.Utilities.enterText;
import static uk.co.malbec.welcometohell.Utilities.waitForPage;

@Step({MotokoQuestion.class, Jurisdiction.Jedi.class})
@Narrative("Star wars question.")
public interface StarWarsQuestion {

    @Narrative("Enter correct answers and go to the done time page.")
    class KnowsStarWars implements StarWarsQuestion {
        @Demands
        private WebDriver webDriver;

        @OnlyRunWith
        Predicate predicate = or(
                and(
                        withStep(EnterGender.EnterFemaleGender.class),
                        withStep(CosPlayQuestion.class)
                ),
                and(
                        withStep(EnterGender.EnterMaleGender.class),
                        not(withStep(CosPlayQuestion.class))
                )
        );

        @When
        public void when() {
            enterText(webDriver, "input[name='boba-fett-ship']", "Slave 1");
            enterText(webDriver, "input[name='first-ship']", "Corellian Corvette");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Get Out Of Here", webDriver.getTitle());
        }
    }

    @Narrative("Enter wrong answers and go to the plea page.")
    class IgnorantStarWars implements StarWarsQuestion {
        @Demands
        private WebDriver webDriver;

        @OnlyRunWith
        Predicate predicate = or(
                and(
                        withStep(EnterGender.EnterFemaleGender.class),
                        withStep(CosPlayQuestion.class)
                ),
                and(
                        withStep(EnterGender.EnterMaleGender.class),
                        not(withStep(CosPlayQuestion.class))
                )
        );

        @When
        public void when() {
            enterText(webDriver, "input[name='boba-fett-ship']", "Mary Rose");
            enterText(webDriver, "input[name='first-ship']", "Resolution");

            webDriver.findElement(By.cssSelector("button[type=submit]")).click();
            waitForPage(webDriver);
        }

        @Then
        public void then() {
            assertEquals("Welcome to Hell | Innocence Declaration", webDriver.getTitle());
        }
    }
}
