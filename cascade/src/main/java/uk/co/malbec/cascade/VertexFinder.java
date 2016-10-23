package uk.co.malbec.cascade;

import uk.co.malbec.cascade.modules.ClasspathScanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VertexFinder<T extends Annotation> {

    private Class<T> clazz;

    public VertexFinder(Class<T> clazz){
        this.clazz = clazz;
    }

    List<Vertex> findVertex(String[] paths, ClasspathScanner classpathScanner){
        List<Vertex> edges = new ArrayList<>();

        for (String path : paths) {
            classpathScanner.initialise(path);
            Set<Class<?>> steps = classpathScanner.getTypesAnnotatedWith(clazz);

            for (Class<?> step : steps) {
                findEdges(edges, step, classpathScanner);
            }
        }
        return edges;
    }

    private void findEdges(List<Vertex> edges, Class<?> clazz, ClasspathScanner classpathScanner) {
        if (clazz.isInterface()) {
            Set<Class> subtypes = classpathScanner.getSubTypesOf(clazz);
            for (Class subType : subtypes) {
                findEdges(edges, subType, classpathScanner);
            }
        } else {
            edges.add(new Vertex(clazz));
        }
    }
}
