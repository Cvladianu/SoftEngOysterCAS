package com.tfl.billing;

import java.time.LocalTime;

public class ControllableClock implements Cloackable {
    private long currentTime = System.currentTimeMillis();
    @Override
    public long currentTimeMillis() {
        return currentTime;
    }

    public void setTime(int hour, int minute, int second) {
        currentTime=1000*(3600*hour+60*minute+second);
    }
}
