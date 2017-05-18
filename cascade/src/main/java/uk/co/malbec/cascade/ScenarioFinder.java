package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.modules.ClasspathScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ScenarioFinder {
    
    List<Scenario> findScenarios(String[] paths, ClasspathScanner classpathScanner){
        List<Scenario> scenarios = new ArrayList<Scenario>();

        for (String path : paths) {
            classpathScanner.initialise(path);
            Set<Class<?>> steps = classpathScanner.getTypesAnnotatedWith(Step.class);

            for (Class<?> step : steps) {
                findScenarios(scenarios, step, classpathScanner, step);
            }
        }
        return scenarios;
    }

    private void findScenarios(List<Scenario> scenarios, Class<?> clazz, ClasspathScanner classpathScanner, Class<?> stateClazz) {

        if (Arrays.stream(stateClazz.getAnnotations())
                .noneMatch(a -> a.annotationType().equals(Step.class))){
            return; //Reflections library will return classes that implement interfaces that have annotations.  We dont' want this yet.
        }

        if (clazz.isInterface()) {
            Set<Class> subtypes = classpathScanner.getSubTypesOf(clazz);
            for (Class subType : subtypes) {
                findScenarios(scenarios, subType, classpathScanner, stateClazz);
            }
        } else {
            scenarios.add(new Scenario(clazz, stateClazz));
        }
    }
}
