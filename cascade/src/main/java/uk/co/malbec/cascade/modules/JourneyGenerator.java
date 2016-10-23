package uk.co.malbec.cascade.modules;


import uk.co.malbec.cascade.Edge;
import uk.co.malbec.cascade.Vertex;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.generator.Filter;

import java.util.List;

public interface JourneyGenerator {
    List<Journey> generateJourneys(List<Edge> edges, List<Vertex> vertices, Class<?> controlClass, Filter filter);

}
