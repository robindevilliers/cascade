package uk.co.malbec.example.steps;

import junit.framework.Assert;
import uk.co.malbec.example.CarCompany;
import uk.co.malbec.example.Requisitioner;
import uk.co.malbec.example.Stores;


@Step({RequisitionStep.class, ShipToNuremberg.class})
public interface SourcePartsStep {

    @Description(", source from requisitioner")
    public class SuccessfulSourcingFromRequisitioner implements SourcePartsStep {

        @Demands
        Stores stores;

        @Demands
        Requisitioner requisitioner;

        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            stores.addStock("Wheel", 0);
            stores.addStock("Steering Wheel", 0);
            stores.addStock("Chassis", 0);
            stores.addStock("Seat", 0);

            requisitioner.addToMarket("Wheel", 30);
            requisitioner.addToMarket("Steering Wheel", 10);
            requisitioner.addToMarket("Chassis", 20);
            requisitioner.addToMarket("Seat", 10);
        }

        @When
        public void execute() {
            System.out.println("Sourcing parts from requisitioner");

            carCompany.sourceComponents();
        }

        @Then
        public void check(Throwable f) {
            Assert.assertNull(f);
        }
    }

    @Description(", source from stores")
    public class SuccessfulSourcingFromStores implements SourcePartsStep {

        @Demands
        Stores stores;

        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            stores.addStock("Wheel", 4);
            stores.addStock("Steering Wheel", 2);
            stores.addStock("Chassis", 8);
            stores.addStock("Seat", 4);
        }

        @When
        public void execute() {
            System.out.println("Sourcing parts from stores");
            carCompany.sourceComponents();
        }

        @Then
        public void check(Throwable f) {
        }
    }

    @Description(" and fail due to no parts")
    @Terminator
    public class UnsuccessfulSourcingDueToNoParts implements SourcePartsStep {

        @Demands
        Stores stores;

        @Demands
        Requisitioner requisitioner;

        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            stores.addStock("Wheel", 0);
            stores.addStock("Steering Wheel", 0);
            stores.addStock("Chassis", 0);
            stores.addStock("Seat", 0);

            requisitioner.addToMarket("Wheel", 10);
            requisitioner.addToMarket("Steering Wheel", 3);
            requisitioner.addToMarket("Chassis", 1);
            requisitioner.addToMarket("Seat", 10);
        }

        @When
        public void execute() {
            System.out.println("FAIL to source parts");
            carCompany.sourceComponents();
        }

        @Then
        public void check(Throwable f) {
            Assert.assertNotNull(f);
            Assert.assertEquals("we can't complete order as we can't buy components", f.getMessage());

        }


    }

}
