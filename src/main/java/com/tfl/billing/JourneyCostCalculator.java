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
    static final BigDecimal LONG_PEAK_JOURNEY_PRICE = new BigDecimal(3.80);
    static final BigDecimal SHORT_PEAK_JOURNEY_PRICE = new BigDecimal(2.90);
    static final BigDecimal LONG_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.70);
    static final BigDecimal SHORT_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(1.60);
    static final String longJourneyDelimitator="25:00";

    public JourneyCostCalculator() {
    }

    public BigDecimal customerTotalFor(List<Journey> journeys)
    {
        BigDecimal customerTotal = new BigDecimal(0);

        for (Journey journey : journeys) {

            customerTotal = customerTotal.add(getJourneyPrice(journey));
        }
        return roundToNearestPenny(customerTotal);
    }

    public BigDecimal getJourneyPrice(Journey journey)
    {
        BigDecimal journeyPrice;
        if (peak(journey)) {
            journeyPrice=SHORT_PEAK_JOURNEY_PRICE;
            if(longJourney(journey))
            {
                journeyPrice=LONG_PEAK_JOURNEY_PRICE;
            }
        }
        else
        {
            journeyPrice=SHORT_OFF_PEAK_JOURNEY_PRICE;
            if(longJourney(journey))
            {
                journeyPrice=LONG_OFF_PEAK_JOURNEY_PRICE;
            }
        }
        return journeyPrice;
    }

    public BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private boolean longJourney(Journey journey) {
        String durationMinutes=journey.durationMinutes();
        //appends a 0 if nb of seconds is 0, becuase in this scenario string looks like "abc:0" instead of "abc:00"
        //nbSeconds%60 yields the accurate number of seconds
        if(journey.durationSeconds()%60==0)
            durationMinutes+="0";
        System.out.println(durationMinutes);

        if(durationMinutes.length()==longJourneyDelimitator.length())
        {
            return durationMinutes.compareTo(longJourneyDelimitator)>=0;
        }
        return durationMinutes.length()>longJourneyDelimitator.length();

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

    public BigDecimal getRoundedLongPeak() {
        return roundToNearestPenny(LONG_PEAK_JOURNEY_PRICE);
    }

    public BigDecimal getRoundedShortPeak() {
        return roundToNearestPenny(SHORT_PEAK_JOURNEY_PRICE);
    }

    public BigDecimal getRoundedLongOffPeak() {
        return roundToNearestPenny(LONG_OFF_PEAK_JOURNEY_PRICE);
    }

    public BigDecimal getRoundedShortOffPeak() {
        return roundToNearestPenny(SHORT_OFF_PEAK_JOURNEY_PRICE);
    }
}
