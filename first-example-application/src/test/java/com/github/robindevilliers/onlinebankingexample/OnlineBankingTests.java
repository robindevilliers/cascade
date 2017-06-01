package com.github.robindevilliers.onlinebankingexample;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.onlinebankingexample.domain.Account;
import com.github.robindevilliers.onlinebankingexample.domain.StandingOrder;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import com.github.robindevilliers.cascade.CascadeRunner;
import com.github.robindevilliers.cascade.Completeness;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.modules.reporter.ListOfStringsStateRendering;
import com.github.robindevilliers.onlinebankingexample.domain.PersonalDetails;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("all")
@RunWith(CascadeRunner.class)
@Scan("com.github.robindevilliers.onlinebankingexample.steps")
@CompletenessLevel(Completeness.SCENARIO_COMPLETE)
@StateRenderingRule(ListOfStringsStateRendering.class)
//@StepPostHandler(WaitASecond.class)
//@StepHandler(TakeScreenshot.class)
public class OnlineBankingTests {

    {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    @Demands
    String username;

    @Demands
    String password;

    @Demands
    String challengePhrase;

    @Demands
    List<String> notices;

    @Demands
    List<Account> accounts;

    @Demands
    PersonalDetails personalDetails;

    @Demands
    List<StandingOrder> standingOrders;

    @Demands
    List<Map> recentPayments;

    @Supplies
    static File REPORTS_BASE_DIRECTORY = new File("./reportsOutput");

    @Demands
    static File REPORTS_DIRECTORY;

    @BeforeAll
    public static void before(List<Journey> journeys){
    }

    @Setup
    public void setup(Journey journey) {

        Map user = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("challengePhrase", challengePhrase);
            put("notices", notices);
            put("accounts", accounts);
            put("personalDetails", personalDetails);
            put("standingOrders", standingOrders);
            put("recentPayments", recentPayments);
        }};

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/database/set-user");
        Response response = target.request().post(Entity.entity(user, "application/json"));


        if (response.getStatus() != 200) {
            String content = response.readEntity(String.class);
            System.err.print(content);
        }

        assertEquals(200, response.getStatus());
    }

    @Teardown
    public static void tearDown(Journey journey){
    }

    @AfterAll
    public static void after(List<Journey> journeys){
        try {
            new ProcessBuilder("open", REPORTS_DIRECTORY.getAbsolutePath() + "/index.html").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
