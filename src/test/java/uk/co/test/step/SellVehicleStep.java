package uk.co.test.step;


import uk.co.test.cascade.annotations.Description;
import uk.co.test.cascade.annotations.Step;
import uk.co.test.cascade.annotations.When;

@Step(DeliverVehicleToShopStep.class)
public interface SellVehicleStep  {

    @Description(", sell vehicle")
    public class Successful implements  SellVehicleStep {

        @When
        public void execute() {
            System.out.println("selling vehicle successfully");
        }

    }

}
