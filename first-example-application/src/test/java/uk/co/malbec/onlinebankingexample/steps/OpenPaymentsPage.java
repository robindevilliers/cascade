package uk.co.malbec.onlinebankingexample.steps;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static uk.co.malbec.cascade.conditions.Predicates.or;
import static uk.co.malbec.cascade.conditions.Predicates.withStep;
import static uk.co.malbec.onlinebankingexample.Utilities.assertRecentPaymentRow;
import static uk.co.malbec.onlinebankingexample.Utilities.assertStandingOrderRow;

@Step(Portfolio.class)
public class OpenPaymentsPage {

    @OnlyRunWith
    Predicate predicate = or(
            withStep(Portfolio.CurrentAccountOnly.class),
            withStep(Portfolio.CurrentAndSaverAccounts.class),
            withStep(Portfolio.AllAccounts.class)
    );

    @Demands
    public WebDriver webDriver;

    @Supplies
    public List<Map<String, String>> standingOrders;

    @Supplies
    public List<Map<String, String>> recentPayments;

    @Given
    public void given() {
        standingOrders = new ArrayList<Map<String, String>>() {{
            add(new HashMap<String, String>() {{
                put("id", "1");
                put("dueDate", "30 Aug 2014");
                put("description", "Lorem ipsum dolor sit amet.");
                put("reference", "10001-02203-222");
                put("period", "Monthly");
                put("amount", "2343");
                put("accountNumber", "93841732");
                put("sortCode", "893810");
            }});
            add(new HashMap<String, String>() {{
                put("id", "2");
                put("dueDate", "30 Sep 2014");
                put("description", "Sed consequat");
                put("reference", "X12345-ROBIN-DE-VILLIERS");
                put("period", "Quarterly");
                put("amount", "1500");
                put("accountNumber", "44433232");
                put("sortCode", "893810");
            }});
            add(new HashMap<String, String>() {{
                put("id", "3");
                put("dueDate", "2 Dec 2014");
                put("description", "Etiam sit amet ");
                put("reference", "11102929920293837X");
                put("period", "Yearly");
                put("amount", "8500");
                put("accountNumber", "23334332");
                put("sortCode", "100210");
            }});
        }};

        recentPayments = new ArrayList<Map<String, String>>() {{
            add(new HashMap<String, String>() {{
                put("date", "30 Aug 2014");
                put("description", "Lorem ipsum dolor sit amet.");
                put("reference", "1112");
                put("accountNumber", "93841732");
                put("sortCode", "893810");
                put("amount", "2343");
                put("cleared", "");
            }});

            add(new HashMap<String, String>() {{
                put("date", "29 Aug 2014");
                put("description", "Aenean imperdiet");
                put("reference", "111223");
                put("accountNumber", "12345678");
                put("sortCode", "100102");
                put("amount", "20000");
                put("cleared", "");
            }});
            add(new HashMap<String, String>() {{
                put("date", "30 May 2014");
                put("description", "Sed consequat");
                put("reference", "1113");
                put("accountNumber", "44433232");
                put("sortCode", "893810");
                put("amount", "1500");
                put("cleared", "2 Jun 2014");
            }});
            add(new HashMap<String, String>() {{
                put("date", "2 Dec 2014");
                put("description", "Etiam sit amet ");
                put("reference", "1114");
                put("accountNumber", "23334332");
                put("sortCode", "100210");
                put("amount", "8500");
                put("cleared", "4 Dec 2014");
            }});

        }};
    }

    @When
    public void when() {
        webDriver.findElement(By.cssSelector("[test-link-payments]")).click();
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
    }

    @Then
    public void then(Throwable f) {
        assertNull(f);
        assertStandingOrderRow(webDriver, "0", "Lorem ipsum dolor sit amet.", "10001-02203-222", "93841732", "893810", "30 Aug 2014", "Monthly", "£ 23.43");
        assertStandingOrderRow(webDriver, "1", "Sed consequat", "X12345-ROBIN-DE-VILLIERS", "44433232", "893810", "30 Sep 2014", "Quarterly", "£ 15.00");
        assertStandingOrderRow(webDriver, "2", "Etiam sit amet", "11102929920293837X", "23334332", "100210", "02 Dec 2014", "Yearly", "£ 85.00");

        assertRecentPaymentRow(webDriver, "0","30 Aug 2014","Lorem ipsum dolor sit amet.","1112","93841732","893810","","£ 23.43");
        assertRecentPaymentRow(webDriver, "1","29 Aug 2014","Aenean imperdiet","111223","12345678","100102","","£ 200.00");
        assertRecentPaymentRow(webDriver, "2","30 May 2014","Sed consequat","1113","44433232","893810","02 Jun 2014","£ 15.00");
        assertRecentPaymentRow(webDriver, "3","02 Dec 2014","Etiam sit amet","1114","23334332","100210","04 Dec 2014","£ 85.00");
    }
}
