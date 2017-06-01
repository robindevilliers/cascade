package com.github.robindevilliers.cascade.modules.filtering

import org.junit.Before
import org.junit.Test
import com.github.robindevilliers.cascade.Scenario
import com.github.robindevilliers.cascade.annotations.FilterTests
import com.github.robindevilliers.cascade.annotations.Step
import com.github.robindevilliers.cascade.conditions.ConditionalLogic
import com.github.robindevilliers.cascade.conditions.Predicate
import com.github.robindevilliers.cascade.conditions.WithStepPredicate
import com.github.robindevilliers.cascade.model.Journey

import static com.github.robindevilliers.cascade.conditions.Predicates.withStep

class StandardFilterStrategyTest {

    StandardFilterStrategy standardFilterStrategy

    @Before
    public void "initialisation"() {
        standardFilterStrategy = new StandardFilterStrategy(new ConditionalLogic())
    }

    @Test
    def void "given instantiation, the filter strategy should extract filter classes from control class"(){
        standardFilterStrategy.init(TestClass, [:])
        assert standardFilterStrategy.predicates[0] == new WithStepPredicate(BadPassword.class)
    }

    @Test
    def void "given no filters specified, the filter strategy should always return true"(){

        standardFilterStrategy.init(TestClassWithNoFilters, [:])

        Journey badPasswordJourney = new Journey([new Scenario(OpenLoginPage.class, OpenLoginPage), new Scenario(BadPassword.class, BadPassword)], TestClass);
        Journey successfulJourney = new Journey([new Scenario(OpenLoginPage.class, OpenLoginPage), new Scenario(Successful.class, Successful)], TestClass);

        assert standardFilterStrategy.match(badPasswordJourney)
        assert standardFilterStrategy.match(successfulJourney)
    }

    @Test
    def void "given many filters specified, the filter strategy should apply and match against any of them"(){

        standardFilterStrategy.init(TestClassWithManyFilters, [:])

        Journey badPasswordJourney = new Journey([new Scenario(OpenLoginPage.class, OpenLoginPage), new Scenario(BadPassword.class, BadPassword)], TestClass);
        Journey successfulJourney = new Journey([new Scenario(OpenLoginPage.class, OpenLoginPage), new Scenario(Successful.class, Successful)], TestClass);

        assert standardFilterStrategy.match(badPasswordJourney)
        assert standardFilterStrategy.match(successfulJourney)
    }

    @Test
    def void "given a filter, the filter strategy should return as appropriate"(){
        standardFilterStrategy.predicates = [new WithStepPredicate(BadPassword)] as Predicate[]

        Journey badPasswordJourney = new Journey([new Scenario(OpenLoginPage.class, OpenLoginPage), new Scenario(BadPassword.class, BadPassword)], TestClass);
        Journey successfulJourney = new Journey([new Scenario(OpenLoginPage.class, OpenLoginPage), new Scenario(Successful.class, Successful)], TestClass);

        assert standardFilterStrategy.match(badPasswordJourney)
        assert !standardFilterStrategy.match(successfulJourney)

        standardFilterStrategy.predicates = [new WithStepPredicate(OpenLoginPage)] as Predicate[]

        assert standardFilterStrategy.match(badPasswordJourney)
        assert standardFilterStrategy.match(successfulJourney)
    }

    @Step
    static class OpenLoginPage {
    }

    @Step([OpenLoginPage])
    static class Successful {
    }

    @Step([OpenLoginPage])
    static class BadPassword {
    }

    public static class TestClass {
        @FilterTests
        Predicate predicate = withStep(BadPassword)
    }

    public static class TestClassWithNoFilters {
    }

    public static class TestClassWithManyFilters {
        @FilterTests
        Predicate predicate1 = withStep(BadPassword)
        @FilterTests
        Predicate predicate2 = withStep(Successful)
    }

}
