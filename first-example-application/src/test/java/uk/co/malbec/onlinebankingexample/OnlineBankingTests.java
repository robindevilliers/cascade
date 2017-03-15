package uk.co.malbec.onlinebankingexample;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import uk.co.malbec.cascade.CascadeRunner;
import uk.co.malbec.cascade.Completeness;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.events.WaitASecond;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.reporter.ListOfStringsStateRendering;
import uk.co.malbec.onlinebankingexample.domain.Account;
import uk.co.malbec.onlinebankingexample.domain.PersonalDetails;
import uk.co.malbec.onlinebankingexample.domain.StandingOrder;

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
import static uk.co.malbec.cascade.conditions.Predicates.and;
import static uk.co.malbec.cascade.conditions.Predicates.stepAt;

@SuppressWarnings("all")
@RunWith(CascadeRunner.class)
@Scan("uk.co.malbec.onlinebankingexample.steps")
@CompletenessLevel(Completeness.SCENARIO_COMPLETE)
@StateRenderingRule(ListOfStringsStateRendering.class)
//@StepPostHandler(WaitASecond.class)
@StepHandler(TakeScreenshot.class)
public class OnlineBankingTests {

    {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    @FilterTests
    Predicate filter = and(
            stepAt(0,uk.co.malbec.onlinebankingexample.steps.OpenLandingPage.class),
            stepAt(1,uk.co.malbec.onlinebankingexample.steps.Login.SuccessfulLogin.class),
            stepAt(2,uk.co.malbec.onlinebankingexample.steps.Challenge.PassChallenge.class),
            stepAt(3,uk.co.malbec.onlinebankingexample.steps.Notice.AcceptOneNotice.class),
            stepAt(4,uk.co.malbec.onlinebankingexample.steps.Portfolio.AllAccounts.class),
            stepAt(5,uk.co.malbec.onlinebankingexample.steps.OpenPaymentsPage.class),
            stepAt(6,uk.co.malbec.onlinebankingexample.steps.CancelStandingOrder.class),
            stepAt(7,uk.co.malbec.onlinebankingexample.steps.OpenPaymentsPage.class),
            stepAt(8,uk.co.malbec.onlinebankingexample.steps.SetupStandingOrder.SetupStandingOrderForLater.class),
            stepAt(9,uk.co.malbec.onlinebankingexample.steps.BackToPorfolio.class),
            stepAt(10,uk.co.malbec.onlinebankingexample.steps.Portfolio.AllAccounts.class),
            stepAt(11,uk.co.malbec.onlinebankingexample.steps.OpenAccountPage.OpenCurrentAccount.class),
            stepAt(12,uk.co.malbec.onlinebankingexample.steps.BackToPorfolio.class),
            stepAt(13,uk.co.malbec.onlinebankingexample.steps.Portfolio.AllAccounts.class),
            stepAt(14,uk.co.malbec.onlinebankingexample.steps.OpenPersonalPage.class),
            stepAt(15,uk.co.malbec.onlinebankingexample.steps.OpenEditMobile.class),
            stepAt(16,uk.co.malbec.onlinebankingexample.steps.EditMobile.class),
            stepAt(17,uk.co.malbec.onlinebankingexample.steps.BackToPorfolio.class),
            stepAt(18,uk.co.malbec.onlinebankingexample.steps.Portfolio.AllAccounts.class),
            stepAt(19,uk.co.malbec.onlinebankingexample.steps.OpenPersonalPage.class),
            stepAt(20,uk.co.malbec.onlinebankingexample.steps.OpenEditAddress.class),
            stepAt(21,uk.co.malbec.onlinebankingexample.steps.EditAddress.class),
            stepAt(22,uk.co.malbec.onlinebankingexample.steps.BackToPorfolio.class),
            stepAt(23,uk.co.malbec.onlinebankingexample.steps.Portfolio.AllAccounts.class),
            stepAt(24,uk.co.malbec.onlinebankingexample.steps.OpenPersonalPage.class),
            stepAt(25,uk.co.malbec.onlinebankingexample.steps.OpenEditEmail.class),
            stepAt(26,uk.co.malbec.onlinebankingexample.steps.EditEmail.class)
    );

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
        System.out.println("tearDown");
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
