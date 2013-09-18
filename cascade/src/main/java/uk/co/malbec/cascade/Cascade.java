package uk.co.malbec.cascade;


import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.FilterTests;
import uk.co.malbec.cascade.annotations.Scan;
import uk.co.malbec.cascade.utils.Reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Cascade {
    
    private ClasspathScanner classpathScanner;

    private ScenarioFinder scenarioFinder;
    
    private JourneyGenerator journeyGenerator;

    private ConstructionStrategy constructionStrategy;

    private TestExecutor testExecutor;

    private Class<?> controlClass;

    private List<Journey> journeys = new ArrayList<Journey>();

    private Class[] filter;
    
    public Cascade(ClasspathScanner classpathScanner,  ScenarioFinder scenarioFinder, JourneyGenerator journeyGenerator, ConstructionStrategy constructionStrategy, TestExecutor testExecutor) {
        this.classpathScanner = classpathScanner;
        this.scenarioFinder = scenarioFinder;
        this.journeyGenerator = journeyGenerator;
        this.constructionStrategy = constructionStrategy;
        this.testExecutor = testExecutor;
    }
    
    public void init(Class<?> controlClass){
        this.controlClass = controlClass;

        FilterTests filterTests = controlClass.getAnnotation(FilterTests.class);

        if (filterTests != null){
            filter = filterTests.value();
        }

        Scan scanAnnotation = controlClass.getAnnotation(Scan.class);

        List<Class> scenarios = scenarioFinder.findScenarios(scanAnnotation.value(), classpathScanner);

        journeys = journeyGenerator.generateJourneys(scenarios);
    }

    public org.junit.runner.Description getDescription() {
        org.junit.runner.Description suite = org.junit.runner.Description.createSuiteDescription("Cascade Tests");
        List<org.junit.runner.Description> descriptions = new ArrayList<org.junit.runner.Description>();
        for (Journey journey : journeys) {
            journey.generateDescription(controlClass);
            descriptions.add(journey.getDescription());
        }

        Collections.sort(descriptions, new Comparator<org.junit.runner.Description>() {
            @Override
            public int compare(org.junit.runner.Description lhs, org.junit.runner.Description rhs) {
                return lhs.getDisplayName().compareTo(rhs.getDisplayName());
            }
        });

        Collections.sort(journeys, new Comparator<Journey>() {
            @Override
            public int compare(Journey lhs, Journey rhs) {

                return lhs.getDescription().getDisplayName().compareTo(rhs.getDescription().getDisplayName());
            }
        });


        for (org.junit.runner.Description description : descriptions){
            suite.addChild(description);
        }

        return suite;
    }


    public void run(RunNotifier notifier) {

        List<org.junit.runner.Description> descriptions = getDescription().getChildren();
        for (int i = 0; i < descriptions.size(); i++) {
            Journey journey = journeys.get(i);

            if (filter != null){
                List<Class> steps = journey.getSteps();
                boolean match = true;
                for (Class clazz: filter){
                    if (!steps.contains(clazz)){
                        match = false;
                        break;
                    }
                }
                if (!match ){
                    continue;
                }
            }


            Reference<Object> control = new Reference<Object>();
            Reference<List<Object>> steps = new Reference<List<Object>>();

            constructionStrategy.setup(controlClass, journey, control, steps);

            testExecutor.executeTest(notifier, descriptions.get(i), steps.get());

            constructionStrategy.tearDown(control, steps);
        }
    }








    
}
