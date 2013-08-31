package uk.co.test;


import java.util.HashMap;
import java.util.Map;

public class Requisitioner {

    private Map<String, Integer> market = new HashMap<String, Integer>();

    public void addToMarket(String name, int quantity) {
        if (market.get(name) == null) {
            market.put(name, quantity);
        } else {
            market.put(name, market.get(name) + quantity);
        }
    }

    public boolean canBuy(String name, int quantity) {
        return market.get(name) >= quantity;
    }

    public void buy(String name, int quantity) {
        market.put(name, market.get(name) - quantity);
    }
}
