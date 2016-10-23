package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.modules.ClasspathScanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EdgeFinder<T extends Annotation> {

    private Class<T> clazz;

    public EdgeFinder(Class<T> clazz){
        this.clazz = clazz;
    }

    List<Edge> findEdges(String[] paths, ClasspathScanner classpathScanner){
        List<Edge> edges = new ArrayList<Edge>();

        for (String path : paths) {
            classpathScanner.initialise(path);
            Set<Class<?>> steps = classpathScanner.getTypesAnnotatedWith(clazz);

            for (Class<?> step : steps) {
                findEdges(edges, step, classpathScanner);
            }
        }
        return edges;
    }

    private void findEdges(List<Edge> edges, Class<?> clazz, ClasspathScanner classpathScanner) {
        if (clazz.isInterface()) {
            Set<Class> subtypes = classpathScanner.getSubTypesOf(clazz);
            for (Class subType : subtypes) {
                findEdges(edges, subType, classpathScanner);
            }
        } else {
            edges.add(new Edge(clazz));
        }
    }
}
