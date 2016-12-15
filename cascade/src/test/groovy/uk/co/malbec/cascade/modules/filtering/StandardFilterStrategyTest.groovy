package uk.co.malbec.cascade.modules.filtering

import org.junit.Before
import org.junit.Test
import uk.co.malbec.cascade.Scenario
import uk.co.malbec.cascade.annotations.FilterTests
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.conditions.ConditionalLogic
import uk.co.malbec.cascade.conditions.Predicate
import uk.co.malbec.cascade.conditions.WithStepPredicate
import uk.co.malbec.cascade.model.Journey

import static uk.co.malbec.cascade.conditions.Predicates.withStep

class StandardFilterStrategyTest {

    StandardFilterStrategy standardFilterStrategy

    @Before
    public void "initialisation"() {
        standardFilterStrategy = new StandardFilterStrategy(new ConditionalLogic())
    }

    @Test
    def void "given instantiation, the filter strategy should extract filter classes from control class"(){
        standardFilterStrategy.init(TestClass)
        assert standardFilterStrategy.predicates == [new WithStepPredicate(BadPassword.class)] as Predicate[]
    }

    @Test
    def void "given no filters specified, the filter strategy should always return true"(){

        standardFilterStrategy.init(TestClassWithNoFilters)

        Journey badPasswordJourney = new Journey([new Scenario(clazz, OpenLoginPage), new Scenario(clazz, BadPassword)], TestClass);
        Journey successfulJourney = new Journey([new Scenario(clazz, OpenLoginPage), new Scenario(clazz, Successful)], TestClass);

        assert standardFilterStrategy.match(badPasswordJourney)
        assert standardFilterStrategy.match(successfulJourney)
    }

    @Test
    def void "given many filters specified, the filter strategy should apply and match against any of them"(){

        standardFilterStrategy.init(TestClassWithManyFilters)

        Journey badPasswordJourney = new Journey([new Scenario(clazz, OpenLoginPage), new Scenario(clazz, BadPassword)], TestClass);
        Journey successfulJourney = new Journey([new Scenario(clazz, OpenLoginPage), new Scenario(clazz, Successful)], TestClass);

        assert standardFilterStrategy.match(badPasswordJourney)
        assert standardFilterStrategy.match(successfulJourney)
    }

    @Test
    def void "given a filter, the filter strategy should return as appropriate"(){
        standardFilterStrategy.predicates = [new WithStepPredicate(BadPassword)] as Predicate[]

        Journey badPasswordJourney = new Journey([new Scenario(clazz, OpenLoginPage), new Scenario(clazz, BadPassword)], TestClass);
        Journey successfulJourney = new Journey([new Scenario(clazz, OpenLoginPage), new Scenario(clazz, Successful)], TestClass);

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
