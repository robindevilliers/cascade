package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Page;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.modules.construction.StandardConstructionStrategy;
import uk.co.malbec.cascade.modules.executor.StandardTestExecutor;
import uk.co.malbec.cascade.modules.filtering.StandardFilterStrategy;
import uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator;
import uk.co.malbec.cascade.modules.scanner.ReflectionsClasspathScanner;

public class CascadeRunner extends Runner {

    private Cascade<Step, Page> cascade;

    public CascadeRunner(Class<?> testClass) {

        //TODO - now that we have a thread pool and image based filtering of redundant journeys in the generator, we don't have a deterministic generator anymore.
        //it can generate different sets of journeys given a different ordering of execution of threads by the operating system.
        cascade = new Cascade<>(new ReflectionsClasspathScanner(),
                new EdgeFinder<>(Step.class),
                new VertexFinder<>(Page.class),
                new StepBackwardsFromTerminatorsJourneyGenerator(new ConditionalLogic(), 10),
                new StandardConstructionStrategy(),
                new StandardTestExecutor(),
                new StandardFilterStrategy(new ConditionalLogic()));
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
