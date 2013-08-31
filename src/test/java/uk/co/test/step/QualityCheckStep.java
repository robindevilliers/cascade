package uk.co.test.step;



import uk.co.test.cascade.annotations.Description;
import uk.co.test.cascade.annotations.Step;
import uk.co.test.cascade.annotations.When;

@Step(AssembleStep.class)
public interface QualityCheckStep  {

    @Description(", quality check")
    public class Successful implements QualityCheckStep {

        @When
        public void execute() {
            System.out.println("checking for quality successfully");
        }
    }

}
