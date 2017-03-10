package uk.co.malbec.cascade.modules.filtering;


import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.annotations.FilterTests;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.conditions.Predicate;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.FilterStrategy;

import java.util.Map;

import static uk.co.malbec.cascade.utils.ReflectionUtils.getValuesOfFieldsAnnotatedWith;
import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class StandardFilterStrategy implements FilterStrategy {

    private Predicate[] predicates;

    private ConditionalLogic conditionalLogic;

    public StandardFilterStrategy(ConditionalLogic conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    @Override
    public void init(Class<?> controlClass, Map<String, Scope> globalScope) {
        predicates = getValuesOfFieldsAnnotatedWith(newInstance(controlClass, "control"), FilterTests.class, Predicate.class);
    }

    @Override
    public boolean match(Journey journey) {
        if (predicates.length == 0){
            return true;
        }

        for (Predicate predicate : predicates) {
            if (conditionalLogic.matches(predicate, journey.getSteps())) {
                return true;
            }
        }
        return false;
    }
}
