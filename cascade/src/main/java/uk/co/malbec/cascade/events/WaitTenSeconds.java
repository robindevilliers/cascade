package uk.co.malbec.cascade.events;


import static uk.co.malbec.cascade.utils.Utils.sleep;

public class WaitTenSeconds implements Handler {
    @Override
    public void handle(Object step) {
        sleep(10000);
    }
}
