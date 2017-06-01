package com.github.robindevilliers.cascade.modules;

import com.github.robindevilliers.cascade.Completeness;
import com.github.robindevilliers.cascade.Scenario;
import com.github.robindevilliers.cascade.Scope;
import com.github.robindevilliers.cascade.modules.reporter.RenderingSystem;

import java.util.List;
import java.util.Map;

public interface Reporter {

    void init(Class<?> controlClass, List<Scenario> scenarios, Map<String, Scope> globalScope, Completeness completenessLevel, RenderingSystem renderingSystem);

    void start();

    TestReport createTestReport();

    void finish();
}
