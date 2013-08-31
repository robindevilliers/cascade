package uk.co.test;


public class Shop {
    
     boolean hasFloorSpace;
    
    public Shop(boolean hasFloorSpace){
        this.hasFloorSpace = hasFloorSpace;
    }
    
    public void acceptNewVehicle(){
        if (!hasFloorSpace)   {
            throw new RuntimeException("shop has no floor space, vehicle returned to factory");
        }
        
    }
    
}
