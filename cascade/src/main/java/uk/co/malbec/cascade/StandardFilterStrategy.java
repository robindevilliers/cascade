package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.FilterTests;
import uk.co.malbec.cascade.model.Journey;

import java.util.List;

public class StandardFilterStrategy implements FilterStrategy {

    private Class[] filter;

    private ConditionalLogic conditionalLogic;

    public StandardFilterStrategy(ConditionalLogic conditionalLogic){
        this.conditionalLogic = conditionalLogic;
    }

    @Override
    public void init(Class<?> controlClass) {
        FilterTests filterTests = controlClass.getAnnotation(FilterTests.class);
        if (filterTests != null){
            filter = filterTests.value();
        }
    }

    @Override
    public boolean match(Journey journey) {

        if (filter != null){
            return conditionalLogic.matches(filter, journey.getSteps());
        }
        return true;
    }
}
