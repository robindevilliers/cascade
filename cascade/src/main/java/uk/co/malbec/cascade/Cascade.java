package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Scan;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.*;
import uk.co.malbec.cascade.utils.Reference;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class Cascade<E extends Annotation, V extends Annotation> {

    private ClasspathScanner classpathScanner;

    private EdgeFinder<E> edgeFinder;

    private VertexFinder<V> vertexFinder;

    private JourneyGenerator journeyGenerator;

    private ConstructionStrategy constructionStrategy;

    private TestExecutor testExecutor;

    private FilterStrategy filterStrategy;

    private Class<?> controlClass;

    private List<Journey> journeys = new ArrayList<Journey>();

    public Cascade(ClasspathScanner classpathScanner,
                   EdgeFinder<E> edgeFinder,
                   VertexFinder<V> vertexFinder,
                   JourneyGenerator journeyGenerator,
                   ConstructionStrategy constructionStrategy,
                   TestExecutor testExecutor,
                   FilterStrategy filterStrategy) {
        this.classpathScanner = classpathScanner;
        this.edgeFinder = edgeFinder;
        this.vertexFinder = vertexFinder;
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
        //TODO - we need validation that a Step/Edge has a When.
        List<Edge> edges = edgeFinder.findEdges(packagesToScan, classpathScanner);
        //TODO - we need validation that a Page/Vertex has a Then.
        List<Vertex> vertices = vertexFinder.findVertex(packagesToScan, classpathScanner);
        journeys = journeyGenerator.generateJourneys(edges, vertices, controlClass, filterStrategy);
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
                for (Thig edge : journey.getTrail()) {
                    String[] tokens = edge.toString().split("\\.");

                    journeyDescription.append(tokens[tokens.length - 1]).append("\n");
                }
                System.err.println(journeyDescription.toString());
                throw e;
            }
        }
    }
}
