package uk.co.malbec.cascade.modules;

import uk.co.malbec.cascade.model.Journey;

import java.util.List;

public interface CompletenessStrategy {
    void init(Class<?> controlClass);

    List<Journey> filter(List<Journey> journeys);
}
