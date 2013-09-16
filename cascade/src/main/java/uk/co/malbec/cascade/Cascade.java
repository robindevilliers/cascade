package uk.co.malbec.cascade;


import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Cascade {
    
    private ClasspathScanner classpathScanner;

    private ScenarioFinder scenarioFinder;
    
    private JourneyGenerator journeyGenerator;

    private Class<?> controlClass;

    private List<Journey> journeys = new ArrayList<Journey>();

    private Class[] filter;
    
    public Cascade(ClasspathScanner classpathScanner,  ScenarioFinder scenarioFinder, JourneyGenerator journeyGenerator) {
        this.classpathScanner = classpathScanner;
        this.scenarioFinder = scenarioFinder;
        this.journeyGenerator = journeyGenerator;
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
            org.junit.runner.Description description = descriptions.get(i);
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




            Map<String, Object> scope = new HashMap<String, Object>();


            Object testContext = newInstance(controlClass);

            //pull supplied objects from malbec context
            for (Field field : controlClass.getDeclaredFields()) {

                Supplies supplies = field.getAnnotation(Supplies.class);
                if (supplies != null) {
                    Object value = getFieldValue(field, testContext);

                    if (value != null) {
                        scope.put(field.getName(), value);
                    }
                }
            }

            //instantiate step objects
            List<Object> steps = new ArrayList<Object>();
            for (Class clazz : journey.getSteps()) {
                steps.add(newInstance(clazz));
            }

            //pull Supplied objects from steps.
            for (Object step : steps) {
                for (Field field : step.getClass().getDeclaredFields()) {
                    Supplies supplies = field.getAnnotation(Supplies.class);
                    if (supplies == null) {
                        continue;
                    }

                    Object value = getFieldValue(field, step);
                    if (value != null) {
                        scope.put(field.getName(), value);
                    }
                }
            }

            //inject Demanded objects.
            for (Object step : steps) {
                for (Field field : step.getClass().getDeclaredFields()) {
                    Demands demands = field.getAnnotation(Demands.class);
                    if (demands == null) {
                        continue;
                    }

                    String fieldName = field.getName();
                    setFieldValue(field, step, scope.get(fieldName));
                }
            }

            //execute Given
            for (Object step : steps) {
                Method givenMethod = null;
                for (Method method : step.getClass().getMethods()) {
                    Given given = method.getAnnotation(Given.class);
                    if (given != null) {
                        givenMethod = method;
                        break;
                    }
                }
                if (givenMethod != null) {
                    try {
                        givenMethod.invoke(step);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            //pull supplied objects from malbec context
            for (Field field : controlClass.getDeclaredFields()) {

                Demands demands = field.getAnnotation(Demands.class);
                if (demands != null) {
                    setFieldValue(field, testContext, scope.get(field.getName()));
                }
            }

            Method intialisationMethod = null;
            for (Method method : controlClass.getMethods()) {
                FinalInitialisation finalInitialisation = method.getAnnotation(FinalInitialisation.class);
                if (finalInitialisation != null) {
                    intialisationMethod = method;
                    break;
                }
            }
            if (intialisationMethod != null) {
                try {
                    intialisationMethod.invoke(testContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //pull supplied objects again

            for (Field field : testContext.getClass().getDeclaredFields()) {
                Supplies supplies = field.getAnnotation(Supplies.class);
                if (supplies == null) {
                    continue;
                }

                Object value = getFieldValue(field, testContext);

                scope.put(field.getName(), value);

            }


            //inject Demanded objects.
            for (Object step : steps) {
                for (Field field : step.getClass().getDeclaredFields()) {
                    Demands demands = field.getAnnotation(Demands.class);
                    if (demands == null) {
                        continue;
                    }

                    String fieldName = field.getName();
                    setFieldValue(field, step, scope.get(fieldName));
                }
            }

            notifier.fireTestStarted(description);

            //execute When and Then
            for (Object step : steps) {
                Method whenMethod = null;
                for (Method method : step.getClass().getMethods()) {
                    When when = method.getAnnotation(When.class);
                    if (when != null) {
                        whenMethod = method;
                        break;
                    }
                }

                Method thenMethod = null;
                for (Method method : step.getClass().getMethods()) {
                    Then then = method.getAnnotation(Then.class);
                    if (then != null) {
                        thenMethod = method;
                        break;
                    }
                }
                Throwable exceptionResult = null;
                if (whenMethod != null) {
                    try {
                        whenMethod.invoke(step);
                    } catch (InvocationTargetException e){
                        exceptionResult = e.getTargetException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (thenMethod != null) {
                    try {
                        thenMethod.invoke(step, exceptionResult);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //notifier.fireTestFailure();

            notifier.fireTestFinished(description);

            //execute Clear
            for (Object step : steps) {
                Method clearMethod = null;
                for (Method method : step.getClass().getMethods()) {
                    Clear clear = method.getAnnotation(Clear.class);
                    if (clear != null) {
                        clearMethod = method;
                        break;
                    }
                }
                if (clearMethod != null) {
                    try {
                        clearMethod.invoke(step);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }







    private static Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getFieldValue(Field field, Object instance) {
        field.setAccessible(true);
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setFieldValue(Field field, Object instance, Object value) {
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
}
