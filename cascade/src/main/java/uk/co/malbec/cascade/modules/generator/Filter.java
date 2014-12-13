package uk.co.malbec.cascade.modules.generator;

import uk.co.malbec.cascade.model.Journey;

public interface Filter {

    boolean match(Journey journey);
}
