package com.github.robindevilliers.cascade.modules.generator;

import com.github.robindevilliers.cascade.model.Journey;

public interface Filter {

    boolean match(Journey journey);
}
