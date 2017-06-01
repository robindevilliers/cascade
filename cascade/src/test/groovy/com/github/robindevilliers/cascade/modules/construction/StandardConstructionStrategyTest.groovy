package com.github.robindevilliers.cascade.modules.construction

import com.github.robindevilliers.cascade.annotations.Clear
import com.github.robindevilliers.cascade.annotations.Demands
import com.github.robindevilliers.cascade.annotations.Given
import com.github.robindevilliers.cascade.annotations.Setup
import com.github.robindevilliers.cascade.annotations.Step
import com.github.robindevilliers.cascade.annotations.Supplies
import com.github.robindevilliers.cascade.annotations.Teardown
import org.junit.Before
import org.junit.Test
import com.github.robindevilliers.cascade.Scenario
import uk.co.malbec.cascade.annotations.*
import com.github.robindevilliers.cascade.model.Journey
import com.github.robindevilliers.cascade.utils.Reference

import static org.mockito.Mockito.mock

class StandardConstructionStrategyTest {

StandardConstructionStrategy standardConstructionStrategy

    static boolean initOnStepCalled
    static boolean initOnControlCalled
    static boolean cleanupOnStepCalled
    static boolean cleanupOnControlCalled

    @Before
    def void "setup"(){
        standardConstructionStrategy = new StandardConstructionStrategy()
        initOnControlCalled = false
        initOnStepCalled = false
        cleanupOnControlCalled = false
        cleanupOnStepCalled = false
    }

    @Test
    def void "given a control class and a journey, the construction strategy should instantiate and bind all participants in a test"(){

        //given
        Journey journey = new Journey([new Scenario(DoStuff.class, DoStuff)], TestControl)
        Reference<Object> controlReference = new Reference<Object>()
        Reference<List<Object>> stepsReference = new Reference<List<Object>>()
        
        //when
        standardConstructionStrategy.setup(TestControl, journey, controlReference, stepsReference, [:])

        //then
        assert initOnControlCalled
        assert initOnStepCalled

        assert controlReference.get() instanceof TestControl
        TestControl control = (TestControl) controlReference.get()
        assert control.d

        assert stepsReference.get().size() == 1
        assert stepsReference.get()[0] instanceof DoStuff

        DoStuff doStuff = (DoStuff) stepsReference.get()[0]
        assert doStuff.c
    }

    def void "given an instantiated set of objects, when tearDown is called, the cleanup methods should be called"(){
        //given
        Reference<Object> controlReference = new Reference<Object>()
        controlReference.set(new TestControl())
        Reference<List<Object>> stepsReference = new Reference<List<Object>>()
        stepsReference.set([new DoStuff()])

        //when
        standardConstructionStrategy.tearDown(controlReference, mock(Journey), stepsReference)

        //then
        assert cleanupOnControlCalled
        assert cleanupOnStepCalled
    }
    
    static class A{}
    static class B{}
    static class C{}
    static class D{}

    @Step
    static class DoStuff {

        @Supplies
        B b = new B()

        @Demands
        A a
        
        @Supplies
        C c

        @Demands
        D d

        @Given
        void init(){
            initOnStepCalled = true
            assert a
            assert !d
            c = new C()
        }

        @Clear
        void cleanup(){
            cleanupOnStepCalled = true
        }

    }

    static class TestControl {
        
        @Supplies
        A a = new A()

        @Demands
        B b
        
        @Supplies
        D d
        
        @Demands
        C c
        
        @Setup
         void init(){
            assert b
            assert c
            initOnControlCalled = true
            d = new D()
        }

        @Teardown
         void cleanup(){
            cleanupOnControlCalled = true
        }
    }
}
