package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@Step(Challenge.class)
public interface Notice {

    public class AcceptOneNotice implements Notice {

        @Supplies
        private List<String> notices = new ArrayList<String>();

        @Demands
        public WebDriver webDriver;

        @Given
        public void given(){
            notices.add("Lorem ipsum dolor sit amet, consectetuer adipiscing elit.");
        }

        @Then
        public void then(Throwable f){
            assertNull(f);

            assertEquals(notices.get(0), webDriver.findElement(By.cssSelector("[test-text-notice]")).getText());

            webDriver.findElement(By.cssSelector("[test-cta-continue]")).click();
            new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));

            assertTrue(webDriver.findElement(By.cssSelector("[test-page-portfolio]")).isDisplayed());
        }


    }

    public class AcceptTwoNotices implements Notice {

        @Supplies
        private List<String> notices = new ArrayList<String>();

        @Demands
        private WebDriver webDriver;

        @Given
        public void given(){
            notices.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
            notices.add("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        }

        @Then
        public void then(Throwable f){
            assertNull(f);

            assertEquals(notices.get(0), webDriver.findElement(By.cssSelector("[test-text-notice]")).getText());

            webDriver.findElement(By.cssSelector("[test-cta-continue]")).click();
            new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));

            assertEquals(notices.get(1), webDriver.findElement(By.cssSelector("[test-text-notice]")).getText());

            webDriver.findElement(By.cssSelector("[test-cta-continue]")).click();
            new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));

            assertTrue(webDriver.findElement(By.cssSelector("[test-page-portfolio]")).isDisplayed());
        }


    }

}
