package uk.co.malbec.cascade;


import uk.co.malbec.cascade.annotations.FilterTests;

import java.util.List;

public class StandardFilterStrategy implements FilterStrategy {

    private Class[] filter;

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
            List<Class> steps = journey.getSteps();
            boolean match = true;
            for (Class clazz: filter){
                if (!steps.contains(clazz)){
                    match = false;
                    break;
                }
            }
            return match;
        }
        return true;
    }
}
