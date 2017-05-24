package uk.co.malbec.example.steps;


import uk.co.malbec.example.CarCompany;
import uk.co.malbec.example.Shop;
import uk.co.malbec.example.Truck;

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
