package uk.co.malbec.cascade.modules;

import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.Scope;
import java.util.List;
import java.util.Map;

public interface Reporter {

    void init(Class<?> controlClass, List<Scenario> scenarios, Map<String, Scope> globalScope);

    void start();

    TestReport createTestReport();

    void finish();
}
