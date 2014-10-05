

package uk.co.malbec.example.steps;


import uk.co.malbec.cascade.annotations.Demands;
import uk.co.malbec.cascade.annotations.Description;
import uk.co.malbec.cascade.annotations.Step;
import uk.co.malbec.cascade.annotations.When;
import uk.co.malbec.example.CarCompany;

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
