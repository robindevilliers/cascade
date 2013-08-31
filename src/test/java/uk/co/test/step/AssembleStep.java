package uk.co.test.step;

import uk.co.test.cascade.annotations.Description;
import uk.co.test.cascade.annotations.Step;
import uk.co.test.cascade.annotations.When;

@Step({SourcePartsStep.class})
public interface AssembleStep {

    @Description(", assemble")
    public class Successful implements AssembleStep {

        @When
        public void execute() {
            System.out.println("assembling successfully");

        }
    }
}
