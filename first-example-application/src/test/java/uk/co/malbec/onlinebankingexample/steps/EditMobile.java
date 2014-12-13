package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.Then;
import uk.co.malbec.cascade.annotations.When;
import uk.co.malbec.cascade.utils.Reference;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.onlinebankingexample.Utilities.*;

@Step(OpenEditMobile.class)
public class EditMobile {

    @Demands
    public WebDriver webDriver;

    @Demands
    public Reference<Boolean> mobileHasBeenEdited;

    @When
    public void when(){
        enterText(webDriver, "[test-input-mobile]", "0789 1234 7765");
        click(webDriver, "[test-save-cta]");
        waitForPage(webDriver);
    }

    @Then
    public void then(Throwable f){
        assertNull(f);
        assertTextEquals(webDriver, "[test-field-mobile]", "0789 1234 7765");
        mobileHasBeenEdited.set(true);
    }
}
