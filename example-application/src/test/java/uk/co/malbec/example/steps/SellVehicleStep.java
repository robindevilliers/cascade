package uk.co.malbec.example.steps;


import uk.co.malbec.cascade.annotations.Description;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.When;

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
