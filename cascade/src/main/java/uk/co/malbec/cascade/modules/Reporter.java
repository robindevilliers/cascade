package uk.co.malbec.cascade.modules;

import uk.co.malbec.cascade.Completeness;
import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.modules.reporter.RenderingSystem;

import java.util.List;
import java.util.Map;

public interface Reporter {

    void init(Class<?> controlClass, List<Scenario> scenarios, Map<String, Scope> globalScope, Completeness completenessLevel, RenderingSystem renderingSystem);

    void start();

    TestReport createTestReport();

    void finish();
}
