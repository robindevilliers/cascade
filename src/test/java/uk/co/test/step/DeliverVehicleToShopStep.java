package uk.co.test.step;


import uk.co.test.CarCompany;
import uk.co.test.Shop;
import uk.co.test.Truck;
import uk.co.test.cascade.annotations.*;

@Step(TestDriveStep.class)
public interface DeliverVehicleToShopStep {

    @Description(", delivery vehicle to shop")
    public class Successful implements DeliverVehicleToShopStep {


        @Supplies
        Truck truck;

        @Supplies
        Shop shop;

        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            truck = new Truck(false);
            shop = new Shop(true);
        }

        @When
        public void execute() {
            System.out.println("delivering to shop successfully");
            carCompany.deliverVehicleToShop();
        }
    }

    @Description(" and have an accident on the way to the store")
    @Terminator
    public class TruckHasAccident implements DeliverVehicleToShopStep {


        @Supplies
        Truck truck;

        @Supplies
        Shop shop;

        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            truck = new Truck(true);
            shop = new Shop(false);
        }

        @When
        public void execute() {
            System.out.println("FAIL truck has accident");
            carCompany.deliverVehicleToShop();
        }
    }


    @Description(" and be delivered to and returned by the shop")
    @Terminator
    public class ShopHasNoSpace implements DeliverVehicleToShopStep {


        @Supplies
        Truck truck;
        
        @Supplies
        Shop shop;

        @Demands
        CarCompany carCompany;

        @Given
        public void setup() {
            truck = new Truck(false);
            shop = new Shop(false);
        }

        @When
        public void execute() {
            System.out.println("FAIL truck has accident");
            carCompany.deliverVehicleToShop();
        }
    }
}
