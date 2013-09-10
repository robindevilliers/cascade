package uk.co.malbec.example.steps;


import uk.co.malbec.cascade.annotations.Description;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.When;

@Step(QualityCheckStep.class)
public interface TestDriveStep  {

    @Description(", malbec drive")
    public class Successful implements TestDriveStep {

        @When
        public void execute() {
            System.out.println("malbec driving successfully");
        }

    }
}
