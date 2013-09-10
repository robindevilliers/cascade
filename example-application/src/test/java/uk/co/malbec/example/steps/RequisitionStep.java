package uk.co.malbec.example.steps;


import static org.junit.Assert.*;
import uk.co.malbec.cascade.annotations.*;
import uk.co.malbec.example.CarCompany;
import uk.co.malbec.example.Order;

@Step
public interface RequisitionStep  {

    @Description("Requisition")
    public class Successful implements RequisitionStep {

        @Supplies
        Order order;
        
        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            order = new Order();
            order.type = "Car";
        }

        @When
        public void execute() {
            System.out.println("requisitioning successfully");
            carCompany.fillOutRequistion(order);
        }

        @Then
        public void check(Throwable f) {
            assertNull(f);
        }
    }

    @Description("Fail due to invalid vehicle type")
    @Terminator
    public class InvalidVehicleOrdered implements RequisitionStep {

        @Supplies
        Order order;
        
        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            order = new Order();
            order.type = "Boat";
        }

        @When
        public void execute() {
            System.out.println("FAIL to requisition");
            carCompany.fillOutRequistion(order);
        }

        @Then
        public void check(Throwable f) {
            assertNotNull(f);
            assertEquals("unable to forfill requisition as we don't see those here.", f.getMessage());
        }
    }


}
