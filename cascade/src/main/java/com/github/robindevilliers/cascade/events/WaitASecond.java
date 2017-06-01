package com.github.robindevilliers.cascade.events;


import static com.github.robindevilliers.cascade.utils.Utils.sleep;

public class WaitASecond implements Handler {

    @Override
    public void handle(Object step) {
        sleep(1000);
    }
}
