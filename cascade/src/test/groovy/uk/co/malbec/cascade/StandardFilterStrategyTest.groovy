package uk.co.malbec.cascade

import org.junit.Before
import org.junit.Test
import uk.co.malbec.cascade.annotations.FilterTests
import uk.co.malbec.cascade.annotations.Step
import uk.co.malbec.cascade.model.Journey

class StandardFilterStrategyTest {

    StandardFilterStrategy standardFilterStrategy

    @Before
    public void "initialisation"() {
        standardFilterStrategy = new StandardFilterStrategy()
    }

    @Test
    def void "given instantiation, the filter strategy should extract filter classes from control class"(){
        standardFilterStrategy.init(TestClass)

        assert standardFilterStrategy.filter == [BadPassword]
    }

    @Test
    def void "given no filters specified, the filter strategy should always return true"(){
        Journey badPasswordJourney = new Journey([OpenLoginPage, BadPassword], TestClass);
        Journey successfulJourney = new Journey([OpenLoginPage, Successful], TestClass);

        assert standardFilterStrategy.match(badPasswordJourney)
        assert standardFilterStrategy.match(successfulJourney)
    }

    @Test
    def void "given a filter, the filter strategy should return as appropriate"(){
        standardFilterStrategy.filter = [BadPassword]

        Journey badPasswordJourney = new Journey([OpenLoginPage, BadPassword], TestClass);
        Journey successfulJourney = new Journey([OpenLoginPage, Successful], TestClass);

        assert standardFilterStrategy.match(badPasswordJourney)
        assert !standardFilterStrategy.match(successfulJourney)

        standardFilterStrategy.filter = [OpenLoginPage]

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

    @FilterTests([BadPassword])
    public static class TestClass {
    }
}
