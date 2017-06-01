package com.github.robindevilliers.welcometohell;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.github.robindevilliers.cascade.annotations.*;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import com.github.robindevilliers.cascade.CascadeRunner;
import com.github.robindevilliers.cascade.Completeness;
import com.github.robindevilliers.cascade.model.Journey;

import java.io.File;
import java.util.List;

@SuppressWarnings("all")
@RunWith(CascadeRunner.class)
@Scan("com.github.robindevilliers.welcometohell.steps")
@CompletenessLevel(Completeness.SCENARIO_COMPLETE)
//@StepHandler(TakeScreenshot.class)
@Parallelize(3)
//@Factory(DisableReporter.class)
//@Limit(2)
//@Profile("standard")
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
    public static void after(List<Journey> journeys) throws Exception{
        new ProcessBuilder("open", REPORTS_DIRECTORY.getAbsolutePath() + "/index.html").start();
    }
}
