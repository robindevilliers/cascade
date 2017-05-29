package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.*;
import uk.co.malbec.cascade.modules.reporter.RenderingSystem;
import uk.co.malbec.cascade.utils.Reference;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static uk.co.malbec.cascade.utils.ReflectionUtils.*;

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

    private Map<String, Scope> globalScope = new HashMap<>();

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

        List<Scenario> scenarios = scenarioFinder.findScenarios(controlClass.getAnnotation(Scan.class).value(), classpathScanner);

        collectStaticSuppliedFields(controlClass, globalScope);
        for (Scenario scenario : scenarios) {
            collectStaticSuppliedFields(scenario.getCls(), globalScope);
        }

        filterStrategy.init(controlClass, globalScope);
        testExecutor.init(controlClass, globalScope);
        completenessStrategy.init(controlClass, globalScope);
        renderingSystem.init(controlClass, globalScope);

        reporter.init(controlClass, scenarios, globalScope);
        List<Journey> journeys = journeyGenerator.generateJourneys(scenarios, controlClass, filterStrategy, globalScope);

        this.journeys = completenessStrategy.filter(journeys);

        for (Scenario scenario : scenarios) {
            injectStaticDemandedFields(scenario.getCls(), globalScope);
        }
        injectStaticDemandedFields(controlClass, globalScope);

        for (Scope scope : globalScope.values()) {
            scope.setGlobal(true);
        }
    }

    public Description getDescription() {
        Description suite = Description.createSuiteDescription("Cascade Tests");
        for (Journey journey : journeys) {
            suite.addChild(journey.getDescription());
        }
        return suite;
    }

    public void run(RunNotifier notifier) {

        invokeStaticAnnotatedMethod(BeforeAll.class, controlClass, new Class[]{List.class}, new Object[]{journeys});

        reporter.start();

        int threads = Optional.ofNullable(controlClass.getAnnotation(Parallelize.class)).map(Parallelize::value).orElse(1);
        CompletionService<TestReport> ecs = new ExecutorCompletionService<>(Executors.newFixedThreadPool(threads));

        journeys.forEach((journey -> {
            TestReport testReport = reporter.createTestReport();

            ecs.submit(() -> {
                try {
                    Reference<Object> control = new Reference<Object>();
                    Reference<List<Object>> steps = new Reference<List<Object>>();

                    Map<String, Scope> scope = constructionStrategy.setup(controlClass, journey, control, steps, globalScope);

                    testReport.setupTest(journey, scope);

                    testReport.startTest(journey, control, steps);

                    testExecutor.executeTest(notifier, journey.getDescription(), steps.get(), journey, testReport, scope);

                    testReport.tearDown(control, steps);

                    constructionStrategy.tearDown(control, journey, steps);

                } catch (RuntimeException e) {
                    //TODO - look at this
                    e.printStackTrace();
                    testReport.handleUnknownException(e, journey);
                } finally {
                    try {
                        testReport.finishTest(journey);
                    } catch (Throwable f) {
                        f.printStackTrace();
                    }
                }
                return testReport;
            });
        }));

        try {
            for (Journey journey : journeys) {
                try {
                    ecs.take().get().mergeTestReport();
                } catch (InterruptedException e) {
                }
            }
        } catch (ExecutionException e) {
            //TODO - handle exception
            e.printStackTrace();
        } finally {
            try {
                reporter.finish();
            } catch (Throwable f) {
                f.printStackTrace();
            }
        }

        invokeStaticAnnotatedMethod(AfterAll.class, controlClass, new Class[]{List.class}, new Object[]{journeys});
    }
}
