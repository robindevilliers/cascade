package uk.co.test;


public class OldStyleTests {

    public void oldStyleTest() {
        Order order = new Order();
        order.type = "Car";

        Stores stores = new Stores();
        stores.addStock("Wheel", 4);
        stores.addStock("Steering Wheel", 2);
        stores.addStock("Chassis", 8);
        stores.addStock("Seat", 3);

        Requisitioner requisitioner = new Requisitioner();
        requisitioner.addToMarket("Wheel", 10);
        requisitioner.addToMarket("Steering Wheel", 3);
        requisitioner.addToMarket("Chassis", 1);
        requisitioner.addToMarket("Seat", 10);

        Truck truck = new Truck(false);
        Shop shop = new Shop(true);

        CarCompany carCompany = new CarCompany(stores, requisitioner, new QualityChecker(true), new TestDriver(true), truck, shop);
        carCompany.fillOutRequistion(order);
        carCompany.sourceComponents();
        carCompany.assemble();
        carCompany.performQualityCheck();
        carCompany.testDrive();
        carCompany.deliverVehicleToShop();
        carCompany.sellVehicle();
        carCompany.registerVehicle();
    }


    public void oldStyleTest2() {
        Order order = new Order();
        order.type = "Motorbike";

        Stores stores = new Stores();
        stores.addStock("Wheel", 2);
        stores.addStock("Handlebars", 0);
        stores.addStock("Chassis", 1);
        stores.addStock("Seat", 1);

        Requisitioner requisitioner = new Requisitioner();
        requisitioner.addToMarket("Wheel", 2);
        requisitioner.addToMarket("Handlebars", 1);
        requisitioner.addToMarket("Chassis", 1);
        requisitioner.addToMarket("Seat", 1);

        Truck truck = new Truck(false);
        Shop shop = new Shop(true);

        CarCompany carCompany = new CarCompany(stores, requisitioner, new QualityChecker(true), new TestDriver(true), truck, shop);
        carCompany.sourceComponents();
        carCompany.fillOutRequistion(order);
        carCompany.shipPartsToNuremberg();
        carCompany.assemble();
        carCompany.performQualityCheck();
        carCompany.testDrive();
        carCompany.deliverVehicleToShop();
        carCompany.sellVehicle();
        carCompany.registerVehicle();
    }
}
