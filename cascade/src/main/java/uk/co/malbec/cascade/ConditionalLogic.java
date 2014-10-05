package uk.co.malbec.cascade;


import java.util.List;

public class ConditionalLogic {
    public boolean matches (Class[] filter, List<Class> steps){
        boolean match = true;
        for (Class clazz: filter){
            if (!steps.contains(clazz)){
                match = false;
                break;
            }
        }
        return match;
    }
}
