package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Scan;
import uk.co.malbec.cascade.annotations.StepPostHandler;
import uk.co.malbec.cascade.annotations.StepPreHandler;
import uk.co.malbec.cascade.model.Journey;
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

    private Class<?> controlClass;

    private List<Journey> journeys = new ArrayList<Journey>();
    
    public Cascade(ClasspathScanner classpathScanner,  ScenarioFinder scenarioFinder, JourneyGenerator journeyGenerator, ConstructionStrategy constructionStrategy, TestExecutor testExecutor, FilterStrategy filterStrategy) {
        this.classpathScanner = classpathScanner;
        this.scenarioFinder = scenarioFinder;
        this.journeyGenerator = journeyGenerator;
        this.constructionStrategy = constructionStrategy;
        this.testExecutor = testExecutor;
        this.filterStrategy = filterStrategy;
    }
    
    public void init(Class<?> controlClass){
        this.controlClass = controlClass;

        filterStrategy.init(controlClass);
        testExecutor.init(controlClass);

        String[] packagesToScan = controlClass.getAnnotation(Scan.class).value();
        List<Class> scenarios = scenarioFinder.findScenarios(packagesToScan, classpathScanner);

        journeys = journeyGenerator.generateJourneys(scenarios, controlClass);
    }

    public Description getDescription() {
        Description suite = Description.createSuiteDescription("Cascade Tests");
        for (Journey journey : journeys) {
            suite.addChild(journey.getDescription());
        }
        return suite;
    }

    public void run(RunNotifier notifier) {
        
        for (Journey journey: journeys){

            if (!filterStrategy.match(journey)) {
                continue;
            }

            Reference<Object> control = new Reference<Object>();
            Reference<List<Object>> steps = new Reference<List<Object>>();

            constructionStrategy.setup(controlClass, journey, control, steps);

            testExecutor.executeTest(notifier, journey.getDescription(), steps.get());

            constructionStrategy.tearDown(control, steps);
        }
    }
}
