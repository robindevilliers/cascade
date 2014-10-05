package uk.co.malbec.onlinebankingexample;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import uk.co.malbec.cascade.CascadeRunner;
import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.FilterTests;
import uk.co.malbec.cascade.annotations.Scan;
import uk.co.malbec.cascade.annotations.Setup;
import uk.co.malbec.onlinebankingexample.steps.Notice;
import uk.co.malbec.onlinebankingexample.steps.Portfolio;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(CascadeRunner.class)
@Scan("uk.co.malbec.onlinebankingexample.steps")

//TODO - add code so that I can defined a step interface here as well as a concrete class.
//TODO - add full AND / OR predicate logic to filters
@FilterTests({  Notice.AcceptTwoNotices.class, Portfolio.CurrentAccountOnly.class})
public class OnlineBankingTests {

    {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

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


    @Setup
    public void setup() {

        Map user = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("challengePhrase", challengePhrase);
            put("notices", notices);
            put("accounts", accounts);

        }};

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/database/set-user");
        Response response = target.request().post(Entity.entity(user, "application/json"));
        assertEquals(200, response.getStatus());
    }

}
