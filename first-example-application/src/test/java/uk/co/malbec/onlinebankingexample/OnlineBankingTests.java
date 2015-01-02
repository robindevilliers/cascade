package uk.co.malbec.onlinebankingexample;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import uk.co.malbec.cascade.CascadeRunner;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.cascade.conditions.Predicates.*;

@RunWith(CascadeRunner.class)
@Scan("uk.co.malbec.onlinebankingexample.steps")
//@StepPostHandler(WaitASecond.class)
public class OnlineBankingTests {

    {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }


   /* @FilterTests
    Predicate filter = and(
            stepAt(0,uk.co.malbec.onlinebankingexample.steps.OpenLandingPage.class),
            stepAt(1,uk.co.malbec.onlinebankingexample.steps.Login.SuccessfulLogin.class),
            stepAt(2,uk.co.malbec.onlinebankingexample.steps.Challenge.PassChallenge.class),
            stepAt(3,uk.co.malbec.onlinebankingexample.steps.Notice.AcceptOneNotice.class),
            stepAt(4,uk.co.malbec.onlinebankingexample.steps.Portfolio.MortgageAccountOnly.class),
            stepAt(5,uk.co.malbec.onlinebankingexample.steps.OpenAccountPage.OpenMortgageAccount.class),
            stepAt(6,uk.co.malbec.onlinebankingexample.steps.BackToPorfolio.class),
            stepAt(7,uk.co.malbec.onlinebankingexample.steps.Portfolio.MortgageAccountOnly.class),
            stepAt(8,uk.co.malbec.onlinebankingexample.steps.OpenPersonalPage.class),
            stepAt(9,uk.co.malbec.onlinebankingexample.steps.OpenEditMobile.class),
            stepAt(10,uk.co.malbec.onlinebankingexample.steps.EditMobile.class),
            stepAt(11,uk.co.malbec.onlinebankingexample.steps.BackToPorfolio.class),
            stepAt(12,uk.co.malbec.onlinebankingexample.steps.Portfolio.MortgageAccountOnly.class),
            stepAt(13,uk.co.malbec.onlinebankingexample.steps.OpenPersonalPage.class),
            stepAt(14,uk.co.malbec.onlinebankingexample.steps.OpenEditEmail.class),
            stepAt(15,uk.co.malbec.onlinebankingexample.steps.EditEmail.class),
            stepAt(16,uk.co.malbec.onlinebankingexample.steps.BackToPorfolio.class),
            stepAt(17,uk.co.malbec.onlinebankingexample.steps.Portfolio.MortgageAccountOnly.class),
            stepAt(18,uk.co.malbec.onlinebankingexample.steps.OpenPersonalPage.class),
            stepAt(19,uk.co.malbec.onlinebankingexample.steps.OpenEditAddress.class),
            stepAt(20,uk.co.malbec.onlinebankingexample.steps.EditAddress.class)
    );*/
    //Predicate filter = or(withStep(Login.FailedLogin.class), withStep(Challenge.FailChallenge.class));
    //Predicate filter = and(withStep(Notice.AcceptOneNotice.class), withStep(Portfolio.CurrentAccountOnly.class),  withStep(EditAddress.class), withStep(EditMobile.class));

    @Demands
    String username;

    @Demands
    String password;

    @Demands
    String challengePhrase;

    @Demands
    List<String> notices;

    @Demands
    List<Map> accounts;

    @Demands
    Map personalDetails;

    @Demands
    List<Map> standingOrders;

    @Demands
    List<Map> recentPayments;

    @Supplies
    List<String[]> expectedStandingOrders = new ArrayList<String[]>();

    @Supplies
    List<String[]> expectedRecentPayments = new ArrayList<String[]>();

    @Setup
    public void setup() {

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

}
