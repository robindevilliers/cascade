package uk.co.test.step;


import uk.co.test.*;
import uk.co.test.cascade.Context;

public class CarContext implements Context {

    public Stores stores = new Stores();
    public Requisitioner requisitioner = new Requisitioner();
    public Truck truck;
    public Shop shop;
    public CarCompany carCompany;
    public QualityChecker qualityChecker;
    public TestDriver testDriver;


    @Override
    public void preSetup() {

    }

    @Override
    public void postSetup() {
        carCompany = new CarCompany(stores, requisitioner, qualityChecker, testDriver, truck, shop);
    }

    @Override
    public void postExecute() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void preCleanup() {

    }

    @Override
    public void postCleanup() {

    }
}
