package com.tfl.billing;

public class ControllableClock implements Cloackable {
    @Override
    public long currentTimeMillis() {
        return 0;
    }
}
