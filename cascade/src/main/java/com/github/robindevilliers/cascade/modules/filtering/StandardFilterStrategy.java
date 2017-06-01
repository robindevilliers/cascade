package com.github.robindevilliers.cascade.modules.filtering;


import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.annotations.FilterTests;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
import com.github.robindevilliers.cascade.conditions.Predicate;
import com.github.robindevilliers.cascade.model.Journey;
import com.github.robindevilliers.cascade.modules.FilterStrategy;
import com.github.robindevilliers.cascade.utils.ReflectionUtils;

import java.util.Map;

public class StandardFilterStrategy implements FilterStrategy {

    private Predicate[] predicates;

    private ConditionalLogic conditionalLogic;

    public StandardFilterStrategy(ConditionalLogic conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    @Override
    public void init(Class<?> controlClass, Map<String, Scope> globalScope) {
        predicates = ReflectionUtils.getValuesOfFieldsAnnotatedWith(ReflectionUtils.newInstance(controlClass, "control"), FilterTests.class, Predicate.class);
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
