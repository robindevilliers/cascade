package uk.co.malbec.example;


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
