package uk.co.malbec.cascade.modules;


import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.generator.Filter;

import java.util.List;

public interface JourneyGenerator {
    public List<Journey> generateJourneys(List<Class> scenarios, Class<?> controlClass, Filter filter);

}
