package uk.co.malbec.cascade;

/**
 * Created by robindevilliers on 02/01/15.
 */
public class Scenario {

    private Class cls;

    public Scenario(Class cls){
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getName(){
        return cls.getName();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scenario scenario = (Scenario) o;

        if (cls != null ? !cls.equals(scenario.cls) : scenario.cls != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cls != null ? cls.hashCode() : 0;
    }
}
