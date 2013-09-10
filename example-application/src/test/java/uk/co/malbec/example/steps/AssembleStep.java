package uk.co.malbec.example.steps;

import uk.co.malbec.cascade.annotations.Description;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.When;

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
