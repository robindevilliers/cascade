package uk.co.test.step;


import junit.framework.Assert;
import uk.co.test.CarCompany;
import uk.co.test.Order;
import uk.co.test.cascade.annotations.*;

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
            Assert.assertNull(f);
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
            Assert.assertNotNull(f);
            Assert.assertEquals("unable to forfill requisition as we don't see those here.", f.getMessage());
        }
    }


}
