package com.github.robindevilliers.cascade;

import com.github.robindevilliers.cascade.annotations.Factory;
import com.github.robindevilliers.cascade.annotations.Profile;
import com.github.robindevilliers.cascade.conditions.ConditionalLogic;
import com.github.robindevilliers.cascade.modules.*;
import com.github.robindevilliers.cascade.modules.executor.StandardTestExecutor;
import com.github.robindevilliers.cascade.modules.reporter.*;
import com.github.robindevilliers.cascade.utils.ReflectionUtils;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import com.github.robindevilliers.cascade.modules.completeness.StandardCompletenessStrategy;
import com.github.robindevilliers.cascade.modules.construction.StandardConstructionStrategy;
import com.github.robindevilliers.cascade.modules.filtering.StandardFilterStrategy;
import com.github.robindevilliers.cascade.modules.generator.StepBackwardsFromTerminatorsJourneyGenerator;
import com.github.robindevilliers.cascade.modules.scanner.ReflectionsClasspathScanner;
import com.github.robindevilliers.cascade.utils.Utils;

public class CascadeRunner extends Runner {

    private Cascade cascade;

    public CascadeRunner(Class<?> testClass) {
        try {
            Profile profile = testClass.getAnnotation(Profile.class);
            if (profile != null) {
                String profileToRun = System.getProperty(profile.name());
                if (profileToRun == null) {
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
            if (factory != null) {
                for (Class<?> clz : factory.value()) {

                    if (ClasspathScanner.class.isAssignableFrom(clz)) {
                        classpathScanner = (ClasspathScanner) ReflectionUtils.newInstance(clz, "classpath scanner");
                        continue;
                    }

                    if (JourneyGenerator.class.isAssignableFrom(clz)) {
                        journeyGenerator = (JourneyGenerator) ReflectionUtils.newInstance(clz, "journey generator");
                        continue;
                    }

                    if (ConstructionStrategy.class.isAssignableFrom(clz)) {
                        constructionStrategy = (ConstructionStrategy) ReflectionUtils.newInstance(clz, "construction strategy");
                        continue;
                    }

                    if (TestExecutor.class.isAssignableFrom(clz)) {
                        testExecutor = (TestExecutor) ReflectionUtils.newInstance(clz, "test exexcutor");
                        continue;
                    }

                    if (FilterStrategy.class.isAssignableFrom(clz)) {
                        filterStrategy = (FilterStrategy) ReflectionUtils.newInstance(clz, "filter strategy");
                        continue;
                    }

                    if (CompletenessStrategy.class.isAssignableFrom(clz)) {
                        completenessStrategy = (CompletenessStrategy) ReflectionUtils.newInstance(clz, "completeness strategy");
                        continue;
                    }

                    if (Reporter.class.isAssignableFrom(clz)) {
                        reporter = (Reporter) ReflectionUtils.newInstance(clz, "reporter");

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
        } catch (Exception e) {
            Utils.printException("Error initialising Cascade.", e);
            cascade = null;
        }
    }

    @Override
    public Description getDescription() {
        if (cascade != null)
            return cascade.getDescription();
        else
            return null;
    }

    @Override
    public void run(RunNotifier notifier) {
        cascade.run(notifier);
    }

}
