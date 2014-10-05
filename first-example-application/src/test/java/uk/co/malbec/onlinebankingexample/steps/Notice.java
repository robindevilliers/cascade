package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.malbec.cascade.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@Step(Challenge.class)
public interface Notice {

    public class AcceptOneNotice implements Notice {

        @Supplies
        private List<String> notices = new ArrayList<String>();

        @Demands
        private WebDriver webDriver;

        @Given
        public void given(){
            notices.add("Lorem ipsum dolor sit amet, consectetuer adipiscing elit.");
        }

        @Then
        public void then(Throwable f){
            assertEquals(notices.get(0), webDriver.findElement(By.cssSelector("[test-text-notice]")).getText());

            webDriver.findElement(By.cssSelector("[test-cta-continue]")).click();

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


        //TODO - need a solution to when then when ... etc. (is this properly done via re-entrancy?
        @Then
        public void then(Throwable f){
            assertEquals(notices.get(0), webDriver.findElement(By.cssSelector("[test-text-notice]")).getText());

            webDriver.findElement(By.cssSelector("[test-cta-continue]")).click();

            assertEquals(notices.get(1), webDriver.findElement(By.cssSelector("[test-text-notice]")).getText());

            webDriver.findElement(By.cssSelector("[test-cta-continue]")).click();

            assertTrue(webDriver.findElement(By.cssSelector("[test-page-portfolio]")).isDisplayed());
        }


    }

}
