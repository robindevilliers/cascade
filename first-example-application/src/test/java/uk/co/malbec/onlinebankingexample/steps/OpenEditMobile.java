package uk.co.malbec.onlinebankingexample.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@Step(OpenPersonalPage.class)
public class OpenEditMobile {

    @Demands
    public WebDriver webDriver;

    @Given
    public void given(){

    }

    @When
    public void when(){
        webDriver.findElement(By.cssSelector("[test-edit-mobile-cta]")).click();
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));

    }

    @Then
    public void then(Throwable f){
        assertNull(f);
        assertEquals(webDriver.findElement(By.cssSelector("[test-current-mobile-text]")).getText(),"0788 1234 567");
    }
}
