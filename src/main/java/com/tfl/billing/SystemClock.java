package com.tfl.billing;

/**
 * Created by cosmi_owugxv5 on 11/28/2017.
 */
public class SystemClock implements ClockI {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
