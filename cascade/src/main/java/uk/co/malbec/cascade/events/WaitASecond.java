package uk.co.malbec.cascade.events;


import static uk.co.malbec.cascade.utils.Utils.sleep;

public class WaitASecond implements Handler {

    @Override
    public void handle(Object step) {
        sleep(1000);
    }
}
