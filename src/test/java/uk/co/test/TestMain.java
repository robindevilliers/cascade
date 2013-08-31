package uk.co.test;


import org.junit.runner.RunWith;
import uk.co.test.cascade.*;
import uk.co.test.cascade.annotations.*;
import uk.co.test.step.RegisterVehicle;
import uk.co.test.step.SourcePartsStep;

@RunWith(CascadeRunner.class)
@Scan("uk.co.test.step")
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

    @FinalInitialisation
    public void init(){
        carCompany = new CarCompany(stores, requisitioner, new QualityChecker(true), new TestDriver(true), truck, shop);
    }

}
