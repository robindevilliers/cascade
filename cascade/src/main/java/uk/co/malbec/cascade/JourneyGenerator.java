package uk.co.malbec.cascade;


import java.util.List;

public interface JourneyGenerator {
    public List<Journey> generateJourneys(List<Class> scenarios);

}
