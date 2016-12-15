package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Scan;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.*;
import uk.co.malbec.cascade.utils.Reference;

import java.util.ArrayList;
import java.util.List;

public class Cascade {

    private ClasspathScanner classpathScanner;

    private ScenarioFinder scenarioFinder;

    private JourneyGenerator journeyGenerator;

    private ConstructionStrategy constructionStrategy;

    private TestExecutor testExecutor;

    private FilterStrategy filterStrategy;

    private CompletenessStrategy completenessStrategy;

    private Class<?> controlClass;

    private List<Journey> journeys = new ArrayList<Journey>();

    public Cascade(ClasspathScanner classpathScanner,
                   ScenarioFinder scenarioFinder,
                   JourneyGenerator journeyGenerator,
                   ConstructionStrategy constructionStrategy,
                   TestExecutor testExecutor,
                   FilterStrategy filterStrategy,
                   CompletenessStrategy completenessStrategy) {
        this.classpathScanner = classpathScanner;
        this.scenarioFinder = scenarioFinder;
        this.journeyGenerator = journeyGenerator;
        this.constructionStrategy = constructionStrategy;
        this.testExecutor = testExecutor;
        this.filterStrategy = filterStrategy;
        this.completenessStrategy = completenessStrategy;
    }

    public void init(Class<?> controlClass){
        this.controlClass = controlClass;

        filterStrategy.init(controlClass);
        testExecutor.init(controlClass);
        completenessStrategy.init(controlClass);

        String[] packagesToScan = controlClass.getAnnotation(Scan.class).value();
        List<Scenario> scenarios = scenarioFinder.findScenarios(packagesToScan, classpathScanner);
        List<Journey> journeys = journeyGenerator.generateJourneys(scenarios, controlClass, filterStrategy);

        this.journeys = completenessStrategy.filter(journeys);
    }

    public Description getDescription() {
        Description suite = Description.createSuiteDescription("Cascade Tests");
        for (Journey journey : journeys) {
            suite.addChild(journey.getDescription());
        }
        return suite;
    }

    public void run(RunNotifier notifier) {

        for (Journey journey : journeys) {
            try {

                Reference<Object> control = new Reference<Object>();
                Reference<List<Object>> steps = new Reference<List<Object>>();

                constructionStrategy.setup(controlClass, journey, control, steps);

                testExecutor.executeTest(notifier, journey.getDescription(), steps.get(), journey);

                constructionStrategy.tearDown(control, steps);
            } catch (RuntimeException e) {
                StringBuilder journeyDescription = new StringBuilder();
                for (Scenario scenario : journey.getSteps()) {
                    String[] tokens = scenario.toString().split("\\.");

                    journeyDescription.append(tokens[tokens.length - 1]).append("\n");
                }
                System.err.println(journeyDescription.toString());
                throw e;
            }
        }
    }
}
