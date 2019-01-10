package com.github.robindevilliers.cascade.modules.generator;


import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
import com.github.robindevilliers.cascade.conditions.Predicate;
import com.github.robindevilliers.cascade.exception.CascadeException;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.utils.ReflectionUtils;
import com.github.robindevilliers.cascade.annotations.OnlyRunWith;
import com.github.robindevilliers.cascade.annotations.Step;
import com.github.robindevilliers.cascade.modules.JourneyGenerator;

import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.synchronizedList;

public class StepBackwardsFromTerminatorsJourneyGenerator implements JourneyGenerator {

    private ConditionalLogic conditionalLogic;
    private MultiplePreStepsGenerator multiplePreStepsGenerator;

    @Override
    public void init(ConditionalLogic conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
        this.multiplePreStepsGenerator = new MultiplePreStepsGenerator();
    }

    public List<Journey> generateJourneys(final List<Scenario> allScenarios, final Class<?> controlClass, Filter filter, Map<String, Scope> globalScope) {

        //sort the scenarios so that the generation of journeys is always deterministic from the users point of view.
        allScenarios.sort(Comparator.comparing(Scenario::getName));

        OnlyRunWithFilter onlyRunWithFilter = new OnlyRunWithFilter(conditionalLogic);
        UnusedScenariosFilter unusedScenariosFilter = new UnusedScenariosFilter(allScenarios);
        RedundantFilter redundantFilter = new RedundantFilter();
        final Filter compositeFilter = new CompositeFilter(onlyRunWithFilter, unusedScenariosFilter, filter, redundantFilter);

        List<Scenario> terminators = new ArrayList<>();

        //these are scenarios marked with a Terminator annotation - explicit terminators.
        findTerminatngScenarios(allScenarios, terminators);

        //these are scenarios marked with a ReEntrantTerminator annotation - these are terminators but only after already being in the journey n number of times.
        findReEntrantTerminatngScenarios(allScenarios, terminators);

        //these are scenarios that belong to steps that are not followed by other steps - implicit terminators.
        findDanglingScenarios(allScenarios, terminators);

        //sort terminators so that we always generate the same journeys.  This guarantees that we always have the same number of tests in the first pass.
        terminators.sort(Comparator.comparing(Scenario::getName));

        final List<Journey> withOutCombinePreStepJourneys = new ArrayList<>();
        for (final Scenario terminator : terminators) {
            generatingTrail(terminator, new ArrayList<>(), allScenarios,
                withOutCombinePreStepJourneys, controlClass,
                compositeFilter);
        }

        final List<Journey> journeys = multiplePreStepsGenerator.insertPreStepsToTheJourneys(withOutCombinePreStepJourneys);

        //go through all scenarios and find scenarios that are not in any journeys and generate an exception if any are found.
        if (!unusedScenariosFilter.getScenarios().isEmpty()) {
            throw new CascadeException(format("Invalid configuration - Orphaned Scenario: Scenario %s not found in any journey: \n" +
                    "\tThis journey generator calculates journeys by finding terminators \n" +
                    "\tand walking backwards to the steps that start journeys. \n" +
                    "\tIf a step is not found in journeys, it is either dependent on \n" +
                    "\tsteps that don't lead to a journey start, or there are no terminators \n" +
                    "\tdownstream of this step.", unusedScenariosFilter.getScenarios().get(0).getClazz().toString()));
        }

        //go through journeys and remove any that are redundant
        //we generate an image of all other journeys and then test if the current journey is redundant.
        Iterator<Journey> journeyIterator = journeys.iterator();
        while (journeyIterator.hasNext()) {
            Journey journey = journeyIterator.next();

            JourneyImage journeyImage = new JourneyImage();

            for (Journey reference : journeys) {
                if (!reference.equals(journey)) {
                    journeyImage.add(reference.getSteps());
                }
            }

            if (journeyImage.match(journey.getSteps())) {
                journeyIterator.remove();
            }
        }

        int index = 1;
        for (Journey journey : journeys) {
            journey.init(index);
            index++;
        }

        journeys.sort(Comparator.comparing(Journey::getName));

        return journeys;
    }

    private void generatingTrail(Scenario currentScenario, List<Scenario> trail, List<Scenario> allScenarios, List<Journey> journeys, Class<?> controlClass, Filter filter) {

        //walk backwards up the trail looking for a repeat of the currentScenario. If one appears before a ReEntrantTerminator, then we have an infinite cycle.
        ListIterator<Scenario> iterator = trail.listIterator(trail.size());
        while (iterator.hasPrevious()) {
            Scenario scenario = iterator.previous();

            //A ReEntrantTerminator has a limit to its entry in a journey.  So there cannot be infinite cycles if we have encountered this here.
            if (scenario.isReEntrantTerminator()) {
                break;
            }

            //Normal terminators are ok too.
            if (scenario.isTerminator()) {
                break;
            }


            //if we find the current scenario in the trail already, without ReEntrantTerminators between, we have an infinite loop.
            if (scenario == currentScenario) {
                List<Scenario> infiniteLoop = new ArrayList<>();

                while (iterator.hasNext()) {
                    infiniteLoop.add(iterator.next());
                }
                infiniteLoop.add(currentScenario);

                Collections.reverse(infiniteLoop);

                StringBuilder buffer = new StringBuilder();
                for (Scenario s : infiniteLoop) {
                    String[] parts = s.getClazz().toString().split("[.]");
                    buffer.append(parts[parts.length - 1]);
                    buffer.append(" ");
                }

                throw new CascadeException(format("Invalid configuration.  An infinite loop is configured. [%s]", buffer.toString().trim()));
            }
        }

        trail.add(currentScenario);

        boolean beginningOfTrail = currentScenario.getSteps()[0] == Step.Null.class;

        if (beginningOfTrail) {
            //we are copying the trail as the current trail may be part of another trail.  There could be a few of them.
            List<Scenario> newTrail = new ArrayList<>(trail);
            Collections.reverse(newTrail);

            Journey candidateJourney = new Journey(newTrail, controlClass);
            if (filter.match(candidateJourney)) {
                journeys.add(candidateJourney);
            }
        } else {

            for (Class<?> precedingStep : currentScenario.getSteps()) {

                Scenario:
                for (Scenario scenario : allScenarios) {

                    //if this scenario doesn't preceed the current step, we don't use it.
                    boolean scenarioIsNotAPrecedingStep = !precedingStep.isAssignableFrom(scenario.getClazz());
                    if (scenarioIsNotAPrecedingStep) {
                        continue;
                    }

                    if (scenario.isTerminator()) {
                        continue;
                    }

                    //if this scenario is a ReEntrantTerminator and its limit is reached, we don't use it.

                    if (scenario.isReEntrantTerminator()) {
                        Integer count = 0;
                        for (Scenario s : trail) {
                            if (s == scenario) {
                                count++;
                            }
                            if (count == scenario.getReEntrantCount()) {
                                continue Scenario;
                            }
                        }
                    }

                    for (Scenario s : trail) {
                        //if the journey already has a scenario for this scenario's step, and it is different from this scenario, we don't use it.
                        if (precedingStep.isAssignableFrom(s.getClazz()) && s != scenario) {
                            continue Scenario;
                        }

                        //if the journey already contains this scenario and its not a 'repeatable' scenario, we don't use it.
                        if (s == scenario && !scenario.isRepeatable()){
                            continue Scenario;
                        }
                    }
                    generatingTrail(scenario, trail, allScenarios, journeys, controlClass, filter);
                }
            }
        }
        trail.remove(trail.size() - 1);
    }

    private void findTerminatngScenarios(List<Scenario> allScenarios, List<Scenario> terminators) {
        for (Scenario scenario : allScenarios) {
            if (scenario.isTerminator()) {
                terminators.add(scenario);
            }
        }
    }

    private void findReEntrantTerminatngScenarios(List<Scenario> allScenarios, List<Scenario> terminators) {
        for (Scenario scenario : allScenarios) {
            if (scenario.isReEntrantTerminator()) {
                terminators.add(scenario);
            }
        }
    }

    private void findDanglingScenarios(List<Scenario> allScenarios, List<Scenario> terminators) {

        //collect all scenarios that don't have @RunWith
        List<Scenario> friendlyScenarios = new ArrayList<>();
        for (Scenario scenario : allScenarios) {
            if (ReflectionUtils.getValueOfFieldAnnotatedWith(ReflectionUtils.newInstance(scenario.getClazz(), "step"), OnlyRunWith.class) == null) {
                friendlyScenarios.add(scenario);
            }
        }


        List<Scenario> possibleTerminators = new ArrayList<>(allScenarios);
        possibleTerminators.removeIf(Scenario::isTerminator);

        //go through the friendly scenarios (scenarios we know will be in a journey, no @RunWith), and remove scenarios that are referenced by them.
        List<Scenario> implicitTerminators = new ArrayList<>(possibleTerminators);
        for (Scenario possibleTerminator : possibleTerminators) {
            for (Scenario scenario : friendlyScenarios) { //if a friendly scenario references the possible terminator, it is not a terminator
                for (Class<?> referencedStep : scenario.getSteps()) {
                    if (referencedStep.isAssignableFrom(possibleTerminator.getClazz())) {
                        implicitTerminators.remove(possibleTerminator);
                    }
                }
            }
        }

        //At this point the scenarios that are remaining in the implicitTerminators list are:
        //1. scenarios that are not referenced by anything
        //2. scenarios that are only referenced by @RunWith scenarios (we will only know if they are really there once we consider the journey)
        terminators.addAll(implicitTerminators);

    }

    private static class CompositeFilter implements Filter {

        private Filter[] filters;

        CompositeFilter(Filter... filters) {
            this.filters = filters;
        }

        @Override
        public boolean match(Journey journey) {
            for (Filter filter : filters) {
                if (!filter.match(journey)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class OnlyRunWithFilter implements Filter {

        private ConditionalLogic conditionalLogic;

        OnlyRunWithFilter(ConditionalLogic conditionalLogic) {
            this.conditionalLogic = conditionalLogic;
        }

        @Override
        public boolean match(Journey journey) {

            for (Scenario step : journey.getSteps()) {
                Predicate predicate = (Predicate) ReflectionUtils.getValueOfFieldAnnotatedWith(ReflectionUtils.newInstance(step.getClazz(), "step"), OnlyRunWith.class);
                if (predicate != null) {
                    boolean valid = conditionalLogic.matches(predicate, journey.getSteps());
                    if (!valid) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static class UnusedScenariosFilter implements Filter {

        private List<Scenario> scenarios;

        UnusedScenariosFilter(List<Scenario> scenarios) {
            this.scenarios = synchronizedList(new ArrayList<>(scenarios));
        }

        @Override
        public boolean match(Journey journey) {

            for (Scenario step : journey.getSteps()) {
                scenarios.remove(step);
            }

            return true;
        }

        public List<Scenario> getScenarios() {
            return scenarios;
        }
    }

    private static class RedundantFilter implements Filter {

        private JourneyImage journeyImage = new JourneyImage();

        @Override
        public synchronized boolean match(Journey journey) {

            if (journeyImage.match(journey.getSteps())) {
                return false;
            } else {
                journeyImage.add(journey.getSteps());
                return true;
            }
        }
    }

    private static class JourneyImage {

        private List<List<Scenario>> image = new ArrayList<>();

        public boolean match(List<Scenario> steps) {

            if (image.size() < steps.size()) {
                return false;
            }

            for (int i = 0; i < steps.size(); i++) {
                if (!image.get(i).contains(steps.get(i))) {
                    return false;
                }
            }
            return true;
        }

        public void add(List<Scenario> steps) {
            for (int i = 0; i < steps.size(); i++) {
                if (image.size() == i) {
                    image.add(new ArrayList<>());
                }
                if (!image.get(i).contains(steps.get(i))) {
                    image.get(i).add(steps.get(i));
                }
            }
        }
    }
}
