package uk.co.malbec.example;


public class Truck {
    
    private boolean hasAccident;
    
    public Truck (boolean hasAccident){
        this.hasAccident = hasAccident;
    }
    
    public void deliverToShop(Shop shop){
        if (hasAccident){
            throw new RuntimeException("truck has accident");
        }
        shop.acceptNewVehicle();

    }
}
