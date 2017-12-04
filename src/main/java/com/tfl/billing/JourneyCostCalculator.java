package com.tfl.billing;

import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cosmi_owugxv5 on 12/4/2017.
 */
public class JourneyCostCalculator {

    static final BigDecimal OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.40);
    static final BigDecimal PEAK_JOURNEY_PRICE = new BigDecimal(3.20);

    public JourneyCostCalculator() {
    }

    public BigDecimal getRoundedOffPeak()
    {
        return roundToNearestPenny(OFF_PEAK_JOURNEY_PRICE);
    }

    public BigDecimal getRoundedPeak()
    {
        return roundToNearestPenny(PEAK_JOURNEY_PRICE);
    }

    public BigDecimal customerTotalFor(List<Journey> journeys)
    {
        BigDecimal customerTotal = new BigDecimal(0);
        for (Journey journey : journeys) {
            BigDecimal journeyPrice = OFF_PEAK_JOURNEY_PRICE;
            if (peak(journey)) {
                journeyPrice = PEAK_JOURNEY_PRICE;
            }
            customerTotal = customerTotal.add(journeyPrice);
        }

        return roundToNearestPenny(customerTotal);
    }

    public BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private boolean peak(Journey journey) {
        return peak(journey.startTime()) || peak(journey.endTime());
    }

    private boolean peak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= 6 && hour <= 9) ||   (hour >= 17 && hour <= 19);
    }
}
