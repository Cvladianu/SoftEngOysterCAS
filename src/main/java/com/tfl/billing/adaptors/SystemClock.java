package com.tfl.billing.adaptors;

import com.tfl.billing.Clock;

/**
 * Created by cosmi_owugxv5 on 11/28/2017.
 */
public class SystemClock implements Clock {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
