package uk.co.test.cascade;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.reflections.Reflections;
import sun.rmi.transport.ObjectTable;
import uk.co.test.cascade.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CascadeRunner extends Runner {

    private Class<?> testClass;

    private List<Journey> journeys = new ArrayList<Journey>();
    
    private Class[] filter;

    public CascadeRunner(Class<?> testClass) {
        this.testClass = testClass;
        
        FilterTests filterTests = testClass.getAnnotation(FilterTests.class);

        if (filterTests != null){
            filter = filterTests.value();
        }
        
        Scan scan = testClass.getAnnotation(Scan.class);

        List<Class> steps = new ArrayList<Class>();

        

        for (String path : scan.value()) {
            Reflections reflections = new Reflections(path);

            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Step.class);
            for (Class<?> clazz : annotated) {
                findStepClasses(steps, clazz, reflections);
            }
        }


        List<Class> terminators = new ArrayList<Class>();
        List<Class> potentialEndings = new ArrayList<Class>(steps);
        for (Class<?> cls : steps) {
            if (cls.isAnnotationPresent(Terminator.class)) {
                terminators.add(cls);
            }

            Step step = findInheritedStepAnnotation(cls);
            if (step.value() != null) {
                for (Class<?> parentStep : step.value()) {
                    Iterator<Class> it = potentialEndings.iterator();
                    while (it.hasNext()) {
                        Class clz = it.next();
                        if (parentStep.isAssignableFrom(clz)) {
                            it.remove();
                        }
                    }
                }
            }
        }
        terminators.addAll(potentialEndings);

        
        
        for (Class cls : terminators) {

            List<Class> trail = new ArrayList<Class>();

            generatingTrail(cls, trail, steps);

        }
    }


    private void generatingTrail(Class cls, List<Class> trail, List<Class> steps) {
        trail.add(cls);

        Step step = findInheritedStepAnnotation(cls);
        if (step.value()[0] == Step.Null.class) {
            List<Class> newTrail = new ArrayList<Class>(trail);
            Collections.reverse(newTrail);
            journeys.add(new Journey(newTrail));
            

        } else {
            for (Class parent : step.value()) {
                for (Class classOfStep : steps) {
                    if (parent.isAssignableFrom(classOfStep)) {
                        if (!classOfStep.isAnnotationPresent(Terminator.class)) {
                            generatingTrail(classOfStep, trail, steps);
                        }
                    }
                }
            }
        }
        trail.remove(trail.size() - 1);
    }


    private Step findInheritedStepAnnotation(Class<?> subject) {
        Step step = subject.getAnnotation(Step.class);
        if (step != null) {
            return step;
        }

        for (Class<?> i : subject.getInterfaces()) {
            step = i.getAnnotation(Step.class);
            if (step != null) {
                return step;
            }
        }

        Class superClass = subject.getSuperclass();
        if (superClass != null) {
            step = findInheritedStepAnnotation(superClass);
        }

        return step;
    }

    private void findStepClasses(List<Class> steps, Class clazz, Reflections reflections) {
        if (clazz.isInterface()) {
            Set subtypes = reflections.getSubTypesOf(clazz);
            for (Object subType : subtypes) {
                Class cls = (Class) subType;
                findStepClasses(steps, cls, reflections);
            }

        } else {

            steps.add(clazz);

        }
    }

    @Override
    public Description getDescription() {
        Description suite = Description.createSuiteDescription("Cascade Tests");
        List<Description> descriptions = new ArrayList<Description>();
        for (Journey journey : journeys) {
            journey.generateDescription(testClass);
            descriptions.add(journey.getDescription());
        }
        
        Collections.sort(descriptions, new Comparator<Description>() {
            @Override
            public int compare(Description lhs, Description rhs) {
                return lhs.getDisplayName().compareTo(rhs.getDisplayName());
            }
        });

        Collections.sort(journeys, new Comparator<Journey>() {
            @Override
            public int compare(Journey lhs, Journey rhs) {

                return lhs.getDescription().getDisplayName().compareTo(rhs.getDescription().getDisplayName());
            }
        });
        
        
        for (Description description : descriptions){
            suite.addChild(description);
        }
        
        return suite;
    }

    @Override
    public void run(RunNotifier notifier) {

        List<Description> descriptions = getDescription().getChildren();
        for (int i = 0; i < descriptions.size(); i++) {
            Description description = descriptions.get(i);
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


            Object testContext = newInstance(testClass);

            //pull supplied objects from test context
            for (Field field : testClass.getDeclaredFields()) {

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


            //pull supplied objects from test context
            for (Field field : testClass.getDeclaredFields()) {

                Demands demands = field.getAnnotation(Demands.class);
                if (demands != null) {
                    setFieldValue(field, testContext, scope.get(field.getName()));
                }
            }

            Method intialisationMethod = null;
            for (Method method : testClass.getMethods()) {
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notifier.fireTestFinished(description);



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
