package uk.co.malbec.example.steps;



import uk.co.malbec.cascade.annotations.Description;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.When;

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
