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
    static final BigDecimal OFF_PEAK_LIMIT = new BigDecimal(7);
    static final BigDecimal PEAK_LIMIT = new BigDecimal(9);
    static final BigDecimal LONG_PEAK_JOURNEY_PRICE = new BigDecimal(3.80);
    static final BigDecimal SHORT_PEAK_JOURNEY_PRICE = new BigDecimal(2.90);
    static final BigDecimal LONG_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.70);
    static final BigDecimal SHORT_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(1.60);
    //static final String longJourneyDelimitator="25:00";
    static final long longJourneyDelimitator=25*60;
    private boolean isPeak;

    public JourneyCostCalculator() {
    }

    public BigDecimal customerTotalFor(List<Journey> journeys)
    {
        BigDecimal customerTotal = new BigDecimal(0);
        this.isPeak=false;
        for (Journey journey : journeys) {
            customerTotal = customerTotal.add(getJourneyPrice(journey));
        }
        customerTotal=withLimits(customerTotal);
        return roundToNearestPenny(customerTotal);
    }

    public BigDecimal getJourneyPrice(Journey journey)
    {
        BigDecimal journeyPrice;
        if (peak(journey)) {
            this.isPeak=true;
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

    private BigDecimal withLimits(BigDecimal journeyPrice)
    {
        if(isPeak)
        {
            if(journeyPrice.compareTo(PEAK_LIMIT)>0)
                journeyPrice=PEAK_LIMIT;
        }
        else
        {
            if(journeyPrice.compareTo(OFF_PEAK_LIMIT)>0)
                journeyPrice=OFF_PEAK_LIMIT;
        }
        return journeyPrice;
    }

    public BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private boolean longJourney(Journey journey) {
        long durationSeconds=journey.durationSeconds();

        return durationSeconds>=longJourneyDelimitator;

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

    public BigDecimal getPeakLimit(){ return roundToNearestPenny(PEAK_LIMIT); }

    public BigDecimal getOffPeakLimit(){ return roundToNearestPenny(OFF_PEAK_LIMIT); }

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
