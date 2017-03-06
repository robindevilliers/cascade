package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.modules.completeness.StandardCompletenessStrategy;
import uk.co.malbec.cascade.modules.construction.StandardConstructionStrategy;
import uk.co.malbec.cascade.modules.executor.StandardTestExecutor;
import uk.co.malbec.cascade.modules.filtering.StandardFilterStrategy;
import uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator;
import uk.co.malbec.cascade.modules.reporter.HtmlReporter;
import uk.co.malbec.cascade.modules.reporter.ListStateRendering;
import uk.co.malbec.cascade.modules.reporter.RenderingSystem;
import uk.co.malbec.cascade.modules.reporter.StringStateRendering;
import uk.co.malbec.cascade.modules.scanner.ReflectionsClasspathScanner;

public class CascadeRunner extends Runner {

    private Cascade cascade;

    public CascadeRunner(Class<?> testClass) {

        RenderingSystem renderingSystem = new RenderingSystem();
        renderingSystem.add(new StringStateRendering());
        renderingSystem.add(new ListStateRendering());


        cascade = new Cascade(new ReflectionsClasspathScanner(),
                new ScenarioFinder(),
                new StepBackwardsFromTerminatorsJourneyGenerator(new ConditionalLogic()),
                new StandardConstructionStrategy(),
                new StandardTestExecutor(),
                new StandardFilterStrategy(new ConditionalLogic()),
                new StandardCompletenessStrategy(),
                new HtmlReporter(renderingSystem),
                renderingSystem
        );
        cascade.init(testClass);
    }

    @Override
    public Description getDescription() {
        return cascade.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        cascade.run(notifier);
    }

}
