package com.github.robindevilliers.cascade;

import com.github.robindevilliers.cascade.annotations.*;
import com.github.robindevilliers.cascade.modules.*;
import com.github.robindevilliers.cascade.utils.Reference;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.modules.reporter.RenderingSystem;
import com.github.robindevilliers.cascade.utils.Utils;

import java.util.*;
import java.util.concurrent.*;

import static com.github.robindevilliers.cascade.utils.ReflectionUtils.collectStaticSuppliedFields;
import static com.github.robindevilliers.cascade.utils.ReflectionUtils.injectStaticDemandedFields;
import static com.github.robindevilliers.cascade.utils.ReflectionUtils.invokeStaticAnnotatedMethod;
import static com.github.robindevilliers.cascade.utils.Utils.printException;

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

    private List<Journey> journeys = new ArrayList<>();

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

        journeyGenerator.init(new ConditionalLogic());

        List<Scenario> scenarios = scenarioFinder.findScenarios(controlClass.getAnnotation(Scan.class).value(), classpathScanner);

        collectStaticSuppliedFields(controlClass, globalScope);
        for (Scenario scenario : scenarios) {
            collectStaticSuppliedFields(scenario.getClazz(), globalScope);
        }

        filterStrategy.init(controlClass, globalScope);
        testExecutor.init(controlClass, globalScope);
        completenessStrategy.init(controlClass, globalScope);
        renderingSystem.init(controlClass, globalScope);

        reporter.init(controlClass, scenarios, globalScope, completenessStrategy.getCompletenessLevel(), renderingSystem);
        List<Journey> journeys = journeyGenerator.generateJourneys(scenarios, controlClass, filterStrategy, globalScope);

        Limit limit = controlClass.getAnnotation(Limit.class);
        if (limit != null){
            journeys = journeys.subList(0, limit.value());
        }

        this.journeys = completenessStrategy.filter(journeys);

        for (Scenario scenario : scenarios) {
            injectStaticDemandedFields(scenario.getClazz(), globalScope);
        }
        injectStaticDemandedFields(controlClass, globalScope);

        for (Scope scope : globalScope.values()) {
            scope.setGlobal();
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
                    Reference<Object> control = new Reference<>();
                    Reference<List<Object>> steps = new Reference<>();

                    Map<String, Scope> scope = constructionStrategy.setup(controlClass, journey, control, steps, globalScope);

                    testReport.setupTest(journey, scope);

                    testReport.startTest(journey, control, steps);

                    testExecutor.executeTest(notifier, journey.getDescription(), steps.get(), journey, testReport, scope);

                    testReport.tearDown(control, steps);

                    constructionStrategy.tearDown(control, journey, steps);

                } catch (RuntimeException e) {
                    printException("Error executing test: " + journey.getId(), e);
                    testReport.handleUnknownException(e, journey);
                } finally {
                    try {
                        testReport.finishTest(journey);
                    } catch (Throwable f) {
                        printException("Error generated by reporter while finalizing test: " + journey.getId(), f);
                    }
                }
                return testReport;
            });
        }));

        try {
            journeys.forEach((j)-> Utils.wrapChecked(() -> ecs.take().get().mergeTestReport()));
        } catch (Exception e) {
            printException("Error merging tests", e);
        } finally {
            try {
                reporter.finish();
            } catch (Throwable f) {
                printException("Error finishing test report", f);
            }
        }
        try {
            invokeStaticAnnotatedMethod(AfterAll.class, controlClass, new Class[]{List.class}, new Object[]{journeys});
        } catch (Exception e){
            printException("Exception executing AfterAll method.", e);
        }
    }
}
