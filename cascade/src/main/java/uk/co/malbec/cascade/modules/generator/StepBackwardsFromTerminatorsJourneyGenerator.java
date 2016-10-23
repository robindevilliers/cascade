package uk.co.malbec.cascade.modules.generator;


import uk.co.malbec.cascade.Edge;
import uk.co.malbec.cascade.Thig;
import uk.co.malbec.cascade.Vertex;
import uk.co.malbec.cascade.annotations.OnlyRunWith;


import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.JourneyGenerator;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.String.format;
import static java.util.Collections.sort;
import static java.util.Collections.synchronizedList;
import static uk.co.malbec.cascade.utils.ReflectionUtils.getValueOfFieldAnnotatedWith;
import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class StepBackwardsFromTerminatorsJourneyGenerator implements JourneyGenerator {

    private ConditionalLogic conditionalLogic;
    private int threadPoolSize;

    public StepBackwardsFromTerminatorsJourneyGenerator(ConditionalLogic conditionalLogic, int threadPoolSize) {
        this.conditionalLogic = conditionalLogic;
        this.threadPoolSize = threadPoolSize;
    }

    public List<Journey> generateJourneys(final List<Edge> allEdges, final List<Vertex> allVertices, final Class<?> controlClass, Filter filter) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        //sort the edges and vertices so that the generation of journeys is always deterministic from the users point of view.
        sort(allEdges, new EdgeComparator());
        sort(allVertices, new VertexComparator());

        OnlyRunWithFilter onlyRunWithFilter = new OnlyRunWithFilter(conditionalLogic);
        UnusedEdgesFilter unusedEdgesFilter = new UnusedEdgesFilter(allEdges);
        UnusedVerticesFilter unusedVerticesFilter = new UnusedVerticesFilter(allVertices);
        RedundantFilter redundantFilter = new RedundantFilter();
        final Filter compositeFilter = new CompositeFilter(onlyRunWithFilter, unusedEdgesFilter, unusedVerticesFilter, filter, redundantFilter);

        List<Vertex> terminators = new ArrayList<>();

        //these are vertices marked with a Terminator annotation - explicit terminators.
        findTerminatingVertices(allVertices, terminators);

        //these are scenarios marked with a ReEntrantTerminator annotation - these are terminators but only after already being in the journey n number of times.
        findReEntrantTerminatngVertices(allVertices, terminators);

        //these are scenarios that belong to steps that are not followed by other steps - implicit terminators.
        findDanglingVertices(allEdges, allVertices, terminators);

        //sort terminators so that we always generate the same journeys.  This guarantees that we always have the same number of tests in the first pass.
        sort(terminators, new VertexComparator());

        final List<Journey> journeys = synchronizedList(new ArrayList<>());
        List<Future> futures = new ArrayList<>();
        for (final Vertex terminator : terminators) {
            futures.add(executorService.submit(() -> {
                generatingTrail(terminator, new ArrayList<>(), allEdges, allVertices, journeys, controlClass, compositeFilter);
                return null;
            }));
        }

        for (Future future : futures) {
            try {
                future.get();//this will generate any exceptions that are thrown in the worker threads.
            } catch (InterruptedException e) {
                //not going to happen
                throw new CascadeException("unexpected interrupted exception thrown from thread pool");
            } catch (ExecutionException e) {
                throw (RuntimeException) e.getCause();
            }
        }

        //go through all scenarios and find scenarios that are not in any journeys and generate an exception if any are found.
        if (!unusedEdgesFilter.getEdges().isEmpty()) {
            throw new CascadeException(format(new StringBuilder()
                    .append("Invalid configuration: Step %s not found in any journey: ")
                    .append("This journey generator calculates journeys by finding terminators ")
                    .append("and walking backwards to the steps that start journeys. ")
                    .append("This step is not transitively connected to a start of a journey")
                    .toString(), unusedEdgesFilter.getEdges().get(0).getCls().toString()));
        }

        if (!unusedVerticesFilter.getVertices().isEmpty()) {
            throw new CascadeException(format(new StringBuilder()
                    .append("Invalid configuration: Vertex %s not found in any journey: ")
                    .append("This journey generator calculates journeys by finding terminators ")
                    .append("and walking backwards to the steps that start journeys. ")
                    .append("This vertex is not transitively connected to a start of a journey")
                    .toString(), unusedEdgesFilter.getEdges().get(0).getCls().toString()));
        }

        //go through journeys and remove any that are redundant
        //we generate an image of all other journeys and then test if the current journey is redundant.
        Iterator<Journey> journeyIterator = journeys.iterator();
        while (journeyIterator.hasNext()) {
            Journey journey = journeyIterator.next();

            JourneyImage journeyImage = new JourneyImage();

            for (Journey reference : journeys) {
                if (!reference.equals(journey)) {
                    journeyImage.add(reference.getTrail());
                }
            }

            if (journeyImage.match(journey.getTrail())) {
                journeyIterator.remove();
            }
        }

        int index = 1;
        for (Journey journey : journeys) {
            journey.init(index);
            index++;
        }

        sort(journeys, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));

        return journeys;
    }

    private void generatingTrail(Thig currentThig, List<Thig> trail, List<Edge> allEdges, List<Vertex> allVertices, List<Journey> journeys, Class<?> controlClass, Filter filter) {

        //walk backwards up the trail looking for a repeat of the currentScenario. If one appears before a ReEntrantTerminator, then we have an infinite cycle.
        ListIterator<Thig> iterator = trail.listIterator(trail.size());
        while (iterator.hasPrevious()) {
            Thig thig = iterator.previous();

            if (thig instanceof Vertex) {
                Vertex vertex = (Vertex) thig;

                //A ReEntrantTerminator has a limit to its entry in a journey.  So there cannot be infinite cycles if we have encountered this here.
                if (vertex.isReEntrantTerminator()) {
                    break;
                }

                //Normal terminators are ok too.
                if (vertex.isTerminator()) {
                    break;
                }
            }

            //if we find the current scenario in the trail already, without ReEntrantTerminators between, we have an infinite loop.
            if (thig == currentThig) {
                List<Thig> infiniteLoop = new ArrayList<>();

                while (iterator.hasNext()) {
                    infiniteLoop.add(iterator.next());
                }
                infiniteLoop.add(currentThig);

                Collections.reverse(infiniteLoop);

                StringBuilder buffer = new StringBuilder();
                for (Thig s : infiniteLoop) {
                    String[] parts = s.getCls().toString().split("[.]");
                    buffer.append(parts[parts.length - 1]);
                    buffer.append(" ");
                }

                throw new CascadeException(format("Invalid configuration.  An infinite loop is configured. [%s]", buffer.toString().trim()));
            }
        }

        trail.add(currentThig);

        boolean beginningOfTrail = currentThig instanceof Edge ? ((Edge) currentThig).getPrecedingVertices()[0] == Step.Null.class : false;

        if (beginningOfTrail) {
            //we are copying the trail as the current trail may be part of another trail.  There could be a few of them.
            List<Thig> newTrail = new ArrayList<>(trail);
            Collections.reverse(newTrail);

            Journey candidateJourney = new Journey(newTrail, controlClass);
            if (filter.match(candidateJourney)) {
                journeys.add(candidateJourney);
            }
        } else {
            if (currentThig instanceof Edge) {
                Edge currentEdge = (Edge) currentThig;


                for (Class<?> precedingVertex : currentEdge.getPrecedingVertices()) {
                    Scenario:
                    for (Vertex vertex : allVertices) {

                        //if this scenario doesn't preceed the current step, we don't use it.
                        boolean vertexIsNotPreceding = !precedingVertex.isAssignableFrom(vertex.getCls());
                        if (vertexIsNotPreceding) {
                            continue;
                        }

                        if (vertex.isTerminator()) {
                            continue;
                        }

                        //if this scenario is a ReEntrantTerminator and its limit is reached, we don't use it.

                        if (vertex.isReEntrantTerminator()) {
                            Integer count = 0;
                            for (Thig s : trail) {
                                if (s == vertex) {
                                    count++;
                                }
                                if (count == vertex.getReEntrantCount()) {
                                    continue Scenario;
                                }
                            }
                        }

                        //if the journey already has a scenario for this scenario's step, and it is different from this scenario, we don't use it.
                        for (Thig s : trail) {
                            if (precedingVertex.isAssignableFrom(s.getCls()) && s != vertex) {
                                continue Scenario;
                            }
                        }
                        generatingTrail(vertex, trail, allEdges, allVertices, journeys, controlClass, filter);
                    }
                }
            }

            if (currentThig instanceof Vertex) {
                Vertex currentVertex = (Vertex) currentThig;

                for (Class<?> precedingEdge : currentVertex.getPrecedingEdges()) {
                    Scenario:
                    for (Edge edge : allEdges) {

                        //if this scenario doesn't preceed the current step, we don't use it.
                        boolean edgeIsNotPreceding = !precedingEdge.isAssignableFrom(edge.getCls());
                        if (edgeIsNotPreceding) {
                            continue;
                        }

                        //if the journey already has a scenario for this scenario's step, and it is different from this scenario, we don't use it.
                        for (Thig s : trail) {
                            if (precedingEdge.isAssignableFrom(s.getCls()) && s != edge) {
                                continue Scenario;
                            }
                        }
                        generatingTrail(edge, trail, allEdges, allVertices, journeys, controlClass, filter);
                    }
                }
            }

        }
        trail.remove(trail.size() - 1);
    }

    private void findTerminatingVertices(List<Vertex> allVertices, List<Vertex> terminators) {
        for (Vertex vertex : allVertices) {
            if (vertex.isTerminator()) {
                terminators.add(vertex);
            }
        }
    }

    private void findReEntrantTerminatngVertices(List<Vertex> allVertices, List<Vertex> terminators) {
        for (Vertex vertex : allVertices) {
            if (vertex.isReEntrantTerminator()) {
                terminators.add(vertex);
            }
        }
    }

    private void findDanglingVertices(List<Edge> allEdges, List<Vertex> allVertices, List<Vertex> terminators) {

        //collect all edges that don't have @RunWith
        List<Edge> friendlyEdges = new ArrayList<>();
        for (Edge edge : allEdges) {
            if (getValueOfFieldAnnotatedWith(newInstance(edge.getCls(), "step"), OnlyRunWith.class) == null) {
                friendlyEdges.add(edge);
            }
        }


        List<Vertex> possibleTerminators = new ArrayList<>(allVertices);
        for (Iterator<Vertex> i = possibleTerminators.iterator(); i.hasNext(); ) {
            Vertex vertex = i.next();
            if (vertex.isTerminator()) {
                i.remove();
            }
        }

        //go through the friendly scenarios (scenarios we know will be in a journey, no @RunWith), and remove scenarios that are referenced by them.
        List<Vertex> implicitTerminators = new ArrayList<>(possibleTerminators);
        for (Vertex possibleTerminator : possibleTerminators) {
            for (Edge edge : friendlyEdges) { //if a friendly edge references the possible terminator vertex, it is not a terminator
                for (Class<?> referencedStep : edge.getPrecedingVertices()) {
                    if (referencedStep.isAssignableFrom(possibleTerminator.getCls())) {
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


    private <T extends Annotation> T findAnnotation(Class<T> annotationClass, Class<?> subject) {
        T step = subject.getAnnotation(annotationClass);
        if (step != null) {
            return step;
        }

        for (Class<?> i : subject.getInterfaces()) {
            step = i.getAnnotation(annotationClass);
            if (step != null) {
                return step;
            }
        }

        Class superClass = subject.getSuperclass();
        if (superClass != null) {
            return findAnnotation(annotationClass, superClass);
        }
        return null;
    }


    private static class CompositeFilter implements Filter {

        private Filter[] filters;

        public CompositeFilter(Filter... filters) {
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

        public OnlyRunWithFilter(ConditionalLogic conditionalLogic) {
            this.conditionalLogic = conditionalLogic;
        }

        @Override
        public boolean match(Journey journey) {

            Iterator<Thig> stepIterator = journey.getTrail().iterator();
            while (stepIterator.hasNext()) {
                Thig step = stepIterator.next();
                Predicate predicate = (Predicate) getValueOfFieldAnnotatedWith(newInstance(step.getCls(), "thig"), OnlyRunWith.class);
                if (predicate != null) {
                    boolean valid = conditionalLogic.matches(predicate, journey.getTrail());
                    if (!valid) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static class UnusedEdgesFilter implements Filter {

        private List<Edge> edges;

        public UnusedEdgesFilter(List<Edge> edges) {
            this.edges = synchronizedList(new ArrayList<>(edges));
        }

        @Override
        public boolean match(Journey journey) {

            for (Thig step : journey.getTrail()) {
                edges.remove(step);
            }

            return true;
        }

        public List<Edge> getEdges() {
            return edges;
        }
    }

    private static class UnusedVerticesFilter implements Filter {

        private List<Vertex> vertices;

        public UnusedVerticesFilter(List<Vertex> vertices) {
            this.vertices = synchronizedList(new ArrayList<>(vertices));
        }

        @Override
        public boolean match(Journey journey) {

            for (Thig step : journey.getTrail()) {
                vertices.remove(step);
            }

            return true;
        }

        public List<Vertex> getVertices() {
            return vertices;
        }
    }

    private static class RedundantFilter implements Filter {

        private JourneyImage journeyImage = new JourneyImage();

        @Override
        public synchronized boolean match(Journey journey) {

            if (journeyImage.match(journey.getTrail())) {
                return false;
            } else {
                journeyImage.add(journey.getTrail());
                return true;
            }
        }
    }

    private static class JourneyImage {

        private List<List<Thig>> image = new ArrayList<>();

        public boolean match(List<Thig> steps) {

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

        public void add(List<Thig> steps) {
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

    private class EdgeComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge lhs, Edge rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }

    private class VertexComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex lhs, Vertex rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }


}
