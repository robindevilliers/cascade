package uk.co.malbec.cascade.modules.filtering;


import uk.co.malbec.cascade.annotations.FilterTests;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.FilterStrategy;

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
