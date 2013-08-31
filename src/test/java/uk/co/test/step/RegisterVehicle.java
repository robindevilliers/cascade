package uk.co.test.step;


import uk.co.test.cascade.annotations.Description;
import uk.co.test.cascade.annotations.Step;
import uk.co.test.cascade.annotations.When;

@Step(SellVehicleStep.class)
public interface RegisterVehicle  {

    @Description(" and register vehicle")
    public class Successful implements  RegisterVehicle {

        @When
        public void execute() {
            System.out.println("registering vehicle successfully");
        }

    }

}
