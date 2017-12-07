package com.tfl.billing.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by cosmi_owugxv5 on 11/28/2017.
 */
public class RandomVals {
    private int min=1;
    private int max=6000;

    public RandomVals() {
    }

    private long randomNum(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public long getRandomMilis()
    {
        return randomNum(this.min,this.max)*1000;
    }
}
