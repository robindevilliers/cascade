package uk.co.malbec.cascade.utils;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static <T> Map<String, ?> map(String name, T o) {
        Map<String, T> map = new HashMap<>();
        map.put(name, o);
        return map;
    }

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //ignore
        }
    }
}
