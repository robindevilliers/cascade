package uk.co.malbec.welcometohell;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import uk.co.malbec.cascade.CascadeRunner;
import uk.co.malbec.cascade.Completeness;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.model.Journey;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("all")
@RunWith(CascadeRunner.class)
@Scan("uk.co.malbec.welcometohell.steps")
@CompletenessLevel(Completeness.SCENARIO_COMPLETE)
//@StepHandler(TakeScreenshot.class)
public class WelcomeToHellTests {

    {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    @Supplies
    static File REPORTS_BASE_DIRECTORY = new File("./reportsOutput");

    @Demands
    static File REPORTS_DIRECTORY;

    @AfterAll
    public static void after(List<Journey> journeys){
        try {
            new ProcessBuilder("open", REPORTS_DIRECTORY.getAbsolutePath() + "/index.html").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
