package uk.co.malbec.example.steps;


import uk.co.malbec.cascade.annotations.Description;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.When;

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
