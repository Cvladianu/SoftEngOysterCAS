package com.tfl.billing.utils;

import com.tfl.billing.Clock;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ControllableClock implements Clock {
    private LocalDateTime now;
    private long epochMilli = System.currentTimeMillis();
    // need to set just the hour, min and seconds, as the current day, month and year will be the same
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
        return epochMilli;
    }

    public void setTime(int hour, int minute, int second) {
        LocalDateTime time;
        time = LocalDateTime.of(year,month,day,hour, minute, second);
        ZoneId zoneId = ZoneId.systemDefault();
        epochMilli = time.atZone(zoneId).toInstant().toEpochMilli();
    }
}
