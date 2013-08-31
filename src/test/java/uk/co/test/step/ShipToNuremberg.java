package uk.co.test.step;


import uk.co.test.CarCompany;
import uk.co.test.Truck;
import uk.co.test.cascade.annotations.Demands;
import uk.co.test.cascade.annotations.Description;
import uk.co.test.cascade.annotations.Step;
import uk.co.test.cascade.annotations.When;

@Step(RequisitionStep.class)
public interface ShipToNuremberg  {

    @Description(", ship to Nuremberg")
    public class Successful implements ShipToNuremberg {

        @Demands
        CarCompany carCompany;

        @When
        public void execute() {
            System.out.println("shipping to nuremberg");
            carCompany.shipPartsToNuremberg();
        }

    }
}
