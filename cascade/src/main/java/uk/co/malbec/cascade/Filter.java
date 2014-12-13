package uk.co.malbec.cascade;

import uk.co.malbec.cascade.model.Journey;

public interface Filter {

    boolean match(Journey journey);
}
