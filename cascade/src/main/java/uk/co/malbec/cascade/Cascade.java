package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Scan;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.*;
import uk.co.malbec.cascade.modules.reporter.RenderingSystem;
import uk.co.malbec.cascade.utils.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cascade {

    private ClasspathScanner classpathScanner;

    private ScenarioFinder scenarioFinder;

    private JourneyGenerator journeyGenerator;

    private ConstructionStrategy constructionStrategy;

    private TestExecutor testExecutor;

    private FilterStrategy filterStrategy;

    private CompletenessStrategy completenessStrategy;

    private Reporter reporter;

    private RenderingSystem renderingSystem;

    private Class<?> controlClass;

    private List<Journey> journeys = new ArrayList<Journey>();

    public Cascade(ClasspathScanner classpathScanner,
                   ScenarioFinder scenarioFinder,
                   JourneyGenerator journeyGenerator,
                   ConstructionStrategy constructionStrategy,
                   TestExecutor testExecutor,
                   FilterStrategy filterStrategy,
                   CompletenessStrategy completenessStrategy,
                   Reporter reporter,
                   RenderingSystem renderingSystem) {
        this.classpathScanner = classpathScanner;
        this.scenarioFinder = scenarioFinder;
        this.journeyGenerator = journeyGenerator;
        this.constructionStrategy = constructionStrategy;
        this.testExecutor = testExecutor;
        this.filterStrategy = filterStrategy;
        this.completenessStrategy = completenessStrategy;
        this.reporter = reporter;
        this.renderingSystem = renderingSystem;
    }

    public void init(Class<?> controlClass) {
        this.controlClass = controlClass;

        filterStrategy.init(controlClass);
        testExecutor.init(controlClass);
        completenessStrategy.init(controlClass);
        renderingSystem.init(controlClass);

        String[] packagesToScan = controlClass.getAnnotation(Scan.class).value();
        List<Scenario> scenarios = scenarioFinder.findScenarios(packagesToScan, classpathScanner);
        reporter.init(controlClass, scenarios);
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

        reporter.start();
        try {
            for (Journey journey : journeys) {
                try {
                    Reference<Object> control = new Reference<Object>();
                    Reference<List<Object>> steps = new Reference<List<Object>>();

                    Map<String, Scope> scope = constructionStrategy.setup(controlClass, journey, control, steps);

                    reporter.setupTest(journey, scope);

                    reporter.startTest(journey, control, steps);

                    testExecutor.executeTest(notifier, journey.getDescription(), steps.get(), journey, reporter);

                    reporter.tearDown(control, steps);

                    constructionStrategy.tearDown(control, steps);
                } catch (RuntimeException e) {
                    //TODO - look at this
                    e.printStackTrace();
                    reporter.handleUnknownException(e, journey);

                } finally {
                    try {
                        reporter.finishTest(journey);
                    } catch (Throwable f) {
                        f.printStackTrace();
                    }
                }
            }
        } finally {
            try {
                reporter.finish();
            } catch (Throwable f) {
                f.printStackTrace();
            }
        }
    }
}
