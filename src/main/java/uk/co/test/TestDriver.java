package uk.co.test;


public class TestDriver {

    public boolean willPass;

    public TestDriver(boolean willPass){
        this.willPass = willPass;
    }

    public void testDrive(){
        if (!willPass){
            throw new RuntimeException("Car has not passed test drive");
        }
    }


}
