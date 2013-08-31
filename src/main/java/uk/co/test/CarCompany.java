package uk.co.test;


import java.util.ArrayList;

public class CarCompany {

    private ArrayList validTypes = new ArrayList();
    {
        validTypes.add("Car");
        validTypes.add("Motorbike");
    }

    private Order order;

    private Stores stores;

    private QualityChecker qualityChecker;

    private TestDriver testDriver;

    private Requisitioner requisitioner;

    private Truck truck;

    private Shop shop;

    public CarCompany(Stores stores, Requisitioner requisitioner, QualityChecker qualityChecker, TestDriver testDriver, Truck truck, Shop shop) {
        this.stores = stores;
        this.requisitioner = requisitioner;
        this.qualityChecker = qualityChecker;
        this.testDriver = testDriver;
        this.truck = truck;
        this.shop = shop;


    }

    public void fillOutRequistion(Order requistionBean) {
        if (!validTypes.contains(requistionBean.type)) {
            throw new RuntimeException("unable to forfill requisition as we don't see those here.");
        }
        this.order = requistionBean;
    }

    public void sourceComponents() {
        if (order.type.equals("Car")) {
            source("Wheel", 4, 20);
            source("Chassis", 1, 5);
            source("Seat", 4, 10);
            source("Steering Wheel", 1, 5);
        }
    }

    public void shipPartsToNuremberg() {
    }

    public void assemble() {

    }

    public void performQualityCheck() {
        qualityChecker.checkQuality();
    }

    public void testDrive() {
        testDriver.testDrive();
    }

    public void deliverVehicleToShop() {
        truck.deliverToShop(shop);
    }

    public void registerVehicle() {

    }

    public void sellVehicle() {

    }


    private void source(String name, int quantity, int buyQuantity) {
        if (!stores.hasStock(name, quantity)) {
            if (requisitioner.canBuy(name, buyQuantity)) {
                requisitioner.buy(name, buyQuantity);
                stores.addStock(name, buyQuantity);
            } else {
                throw new RuntimeException("we can't complete order as we can't buy components");
            }
        }

        stores.getStock(name, quantity);
    }


}
