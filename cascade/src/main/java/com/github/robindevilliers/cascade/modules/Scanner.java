package com.github.robindevilliers.cascade.modules;


import com.github.robindevilliers.cascade.Scenario;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public interface Scanner {

    List<Scenario> findScenarios(String[] paths);
}
