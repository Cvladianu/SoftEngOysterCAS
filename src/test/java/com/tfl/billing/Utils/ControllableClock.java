package com.tfl.billing.Utils;

import com.tfl.billing.Clock;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ControllableClock implements Clock {
    private LocalDateTime time;
    private LocalDateTime now;
    private long epoch = System.currentTimeMillis();
    // need to set just the hour, min and seconds, we get the current day and month
    private int day;
    private int month;
    private int year;

    public ControllableClock()
    {
        now = LocalDateTime.now();
        month = now.getMonthValue();
        day = now.getDayOfMonth();
        year = now.getYear();
    }
    @Override
    public long currentTimeMillis() {
        return epoch;
    }

    public void setTime(int hour, int minute, int second) {
        time = LocalDateTime.of(year,month,day,hour, minute, second);
        ZoneId zoneId = ZoneId.systemDefault();
        epoch = time.atZone(zoneId).toInstant().toEpochMilli();
    }

}