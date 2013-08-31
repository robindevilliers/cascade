package uk.co.test;


import java.util.HashMap;
import java.util.Map;

public class Stores {
    
    private Map<String, Integer> stock = new HashMap<String, Integer>();
    
    public void addStock(String name, int quantity){
        if (stock.get(name) == null){
            stock.put(name, quantity);
        } else {
            stock.put(name, stock.get(name) + quantity);
        }
    }
    
    public boolean hasStock(String name, int quantity){
        return stock.get(name) >= quantity;
    }
    
    public void getStock(String name, int quantity){
        stock.put(name, stock.get(name) - quantity);
    }
    
    
}
