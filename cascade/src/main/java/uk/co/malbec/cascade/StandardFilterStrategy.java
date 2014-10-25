package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.FilterTests;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;

import java.util.List;

import static uk.co.malbec.cascade.utils.ReflectionUtils.getValueOfFieldAnnotatedWith;
import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class StandardFilterStrategy implements FilterStrategy {

    private Predicate filter;

    private ConditionalLogic conditionalLogic;

    public StandardFilterStrategy(ConditionalLogic conditionalLogic){
        this.conditionalLogic = conditionalLogic;
    }

    @Override
    public void init(Class<?> controlClass) {
        filter = (Predicate) getValueOfFieldAnnotatedWith(newInstance(controlClass, "control"), FilterTests.class);
    }

    @Override
    public boolean match(Journey journey) {
        if (filter != null){
            return conditionalLogic.matches(filter, journey.getSteps());
        }
        return true;
    }
}
