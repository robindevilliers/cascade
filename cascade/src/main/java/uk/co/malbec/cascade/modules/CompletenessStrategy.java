package uk.co.malbec.cascade.modules;

import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.model.Journey;

import java.util.List;
import java.util.Map;

public interface CompletenessStrategy {
    void init(Class<?> controlClass, Map<String, Scope> globalScope);

    List<Journey> filter(List<Journey> journeys);
}
