package uk.co.malbec.cascade.handler;


public class WaitTenSeconds implements Handler {
    @Override
    public void handle(Object step) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {}
    }
}
