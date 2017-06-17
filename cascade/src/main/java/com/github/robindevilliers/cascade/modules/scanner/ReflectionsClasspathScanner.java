package com.github.robindevilliers.cascade.modules.scanner;


import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.modules.Scanner;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ReflectionsClasspathScanner implements Scanner {

    @Override
    public List<Scenario> findScenarios(String[] paths) {
        List<Scenario> scenarios = new ArrayList<>();

        for (String path : paths) {
            Reflections reflections = new Reflections(path);

            List<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Step.class)
                    .stream()
                    .filter(clazz -> stream(clazz.getAnnotations()).anyMatch(a -> a.annotationType().equals(Step.class)))
                    .collect(toList());

            for (Class<?> step : typesAnnotatedWith) {
                walk(step, step, scenarios, reflections);
            }
        }
        return scenarios;
    }

    private void walk(Class<?> step, Class clazz, List<Scenario> scenarios, Reflections reflections) {

        if (clazz.isInterface()) {
            Set<Class> subtypes = reflections.getSubTypesOf(clazz);
            for (Class subType : subtypes) {
                walk(step, subType, scenarios, reflections);
            }
        } else {
            scenarios.add(new Scenario(clazz, step));
        }
    }
}
