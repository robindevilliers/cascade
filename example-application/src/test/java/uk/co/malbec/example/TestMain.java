package uk.co.malbec.example;


import org.junit.runner.RunWith;
import uk.co.malbec.cascade.*;
import uk.co.malbec.cascade.annotations.*;

@RunWith(CascadeRunner.class)
@Scan("uk.co.malbec.example.steps")
//@FilterTests({SourcePartsStep.SuccessfulSourcingFromRequisitioner.class, RegisterVehicle.Successful.class})
public class TestMain {

    @Supplies
    Stores stores = new Stores();

    @Supplies
    Requisitioner requisitioner = new Requisitioner();

    @Supplies
    CarCompany carCompany;
    
    @Demands
    Truck truck;
    
    @Demands
    Shop shop;

    @Setup
    public void init(){
        carCompany = new CarCompany(stores, requisitioner, new QualityChecker(true), new TestDriver(true), truck, shop);
    }
}
