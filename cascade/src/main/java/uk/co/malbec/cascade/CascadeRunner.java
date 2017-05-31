package uk.co.malbec.cascade;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import uk.co.malbec.cascade.annotations.Factory;
import uk.co.malbec.cascade.annotations.Profile;
import uk.co.malbec.cascade.conditions.ConditionalLogic;
import uk.co.malbec.cascade.modules.*;
import uk.co.malbec.cascade.modules.completeness.StandardCompletenessStrategy;
import uk.co.malbec.cascade.modules.construction.StandardConstructionStrategy;
import uk.co.malbec.cascade.modules.executor.StandardTestExecutor;
import uk.co.malbec.cascade.modules.filtering.StandardFilterStrategy;
import uk.co.malbec.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator;
import uk.co.malbec.cascade.modules.reporter.*;
import uk.co.malbec.cascade.modules.scanner.ReflectionsClasspathScanner;

import static uk.co.malbec.cascade.utils.ReflectionUtils.newInstance;

public class CascadeRunner extends Runner {

    private Cascade cascade;

    public CascadeRunner(Class<?> testClass) {

        Profile profile = testClass.getAnnotation(Profile.class);
        if (profile != null){
            String profileToRun = System.getProperty(profile.name());
            if (profileToRun == null){
                profileToRun = System.getenv(profile.name());
            }
            if (!profile.value().equals(profileToRun))
                return;
        }

        RenderingSystem renderingSystem = new RenderingSystem();
        renderingSystem.add(new StringStateRendering());
        renderingSystem.add(new ListStateRendering());
        renderingSystem.add(new FileStateRendering());

        ClasspathScanner classpathScanner = new ReflectionsClasspathScanner();
        JourneyGenerator journeyGenerator = new StepBackwardsFromTerminatorsJourneyGenerator();
        ConstructionStrategy constructionStrategy = new StandardConstructionStrategy();
        TestExecutor testExecutor = new StandardTestExecutor();
        FilterStrategy filterStrategy = new StandardFilterStrategy(new ConditionalLogic());
        CompletenessStrategy completenessStrategy = new StandardCompletenessStrategy();
        Reporter reporter = new HtmlReporter();

        Factory factory = testClass.getAnnotation(Factory.class);
        if (factory != null){
            for (Class<?> clz : factory.value()){

                if (ClasspathScanner.class.isAssignableFrom(clz)){
                    classpathScanner = (ClasspathScanner) newInstance(clz, "classpath scanner");
                    continue;
                }

                if (JourneyGenerator.class.isAssignableFrom(clz)){
                    journeyGenerator = (JourneyGenerator) newInstance(clz, "journey generator");
                    continue;
                }

                if (ConstructionStrategy.class.isAssignableFrom(clz)){
                    constructionStrategy = (ConstructionStrategy) newInstance(clz, "construction strategy");
                    continue;
                }

                if (TestExecutor.class.isAssignableFrom(clz)){
                    testExecutor = (TestExecutor) newInstance(clz, "test exexcutor");
                    continue;
                }

                if (FilterStrategy.class.isAssignableFrom(clz)){
                    filterStrategy = (FilterStrategy) newInstance(clz, "filter strategy");
                    continue;
                }

                if (CompletenessStrategy.class.isAssignableFrom(clz)){
                    completenessStrategy = (CompletenessStrategy) newInstance(clz, "completeness strategy");
                    continue;
                }

                if (Reporter.class.isAssignableFrom(clz)){
                    reporter = (Reporter) newInstance(clz, "reporter");

                }

            }
        }

        cascade = new Cascade(classpathScanner,
                new ScenarioFinder(),
                journeyGenerator,
                constructionStrategy,
                testExecutor,
                filterStrategy,
                completenessStrategy,
                reporter,
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
