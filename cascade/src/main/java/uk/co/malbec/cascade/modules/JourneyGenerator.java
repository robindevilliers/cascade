package uk.co.malbec.cascade.modules;


import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.generator.Filter;

import java.util.List;

public interface JourneyGenerator {
    public List<Journey> generateJourneys(List<Scenario> scenarios, Class<?> controlClass, Filter filter);

}
