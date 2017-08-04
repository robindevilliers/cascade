package com.github.robindevilliers.onlinebankingexample.steps;

import com.github.robindevilliers.cascade.annotations.Repeatable;
import org.openqa.selenium.WebDriver;
import com.github.robindevilliers.cascade.annotations.Demands;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.annotations.When;

import static com.github.robindevilliers.onlinebankingexample.Utilities.click;
import static com.github.robindevilliers.onlinebankingexample.Utilities.waitForPage;

@Repeatable
@Step({EditAddress.class, EditEmail.class, EditMobile.class, OpenAccountPage.class, SetupStandingOrder.class})
public class BackToPorfolio {

    @Demands
    public WebDriver webDriver;

    @When
    public void when() {
        click(webDriver, "[test-link-portfolio]");
        waitForPage(webDriver);
    }
}
