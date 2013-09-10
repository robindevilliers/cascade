package uk.co.malbec.example;


public class QualityChecker {
    
    public boolean willPass;
    
    public QualityChecker(boolean willPass){
        this.willPass = willPass;
    }
    
    public void checkQuality(){
        if (!willPass){
            throw new RuntimeException("Car has not passed quality check");
        }
    }
    
    
}
