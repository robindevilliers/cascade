package uk.co.test.step;


import uk.co.test.cascade.annotations.Description;
import uk.co.test.cascade.annotations.Step;
import uk.co.test.cascade.annotations.When;

@Step(QualityCheckStep.class)
public interface TestDriveStep  {

    @Description(", test drive")
    public class Successful implements TestDriveStep {

        @When
        public void execute() {
            System.out.println("test driving successfully");
        }

    }
}
