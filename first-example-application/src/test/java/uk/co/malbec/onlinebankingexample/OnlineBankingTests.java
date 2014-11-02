package uk.co.malbec.onlinebankingexample;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import uk.co.malbec.cascade.CascadeRunner;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.handler.WaitASecond;
import uk.co.malbec.onlinebankingexample.steps.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static uk.co.malbec.cascade.conditions.Predicates.and;
import static uk.co.malbec.cascade.conditions.Predicates.or;
import static uk.co.malbec.cascade.conditions.Predicates.withStep;

@RunWith(CascadeRunner.class)
@Scan("uk.co.malbec.onlinebankingexample.steps")
//@StepPostHandler(WaitASecond.class)
public class OnlineBankingTests {

    {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    //@FilterTests
    //Predicate filter = and(withStep(Notice.AcceptOneNotice.class), withStep(Portfolio.CurrentAccountOnly.class), withStep(OpenPaymentsPage.class));
    //Predicate filter = withStep(OpenEditAddress.class);

   // @FilterTests
    /*Predicate filter = and(
            withStep(OpenLandingPage.class),
            withStep(Login.SuccessfulLogin.class),
            or(
                    withStep(Challenge.FailChallenge.class),
                    and(
                            withStep(Notice.AcceptOneNotice.class),
                            withStep(OpenAccountPage.OpenCurrentAccount.class)
                    )
            )
    );*/

    //TODO - need to fail the test if a variable is demanded but not supplied
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


        if (response.getStatus() != 200){
            String content = response.readEntity(String.class);
            System.err.print(content);
        }

        assertEquals(200, response.getStatus());
    }

}
