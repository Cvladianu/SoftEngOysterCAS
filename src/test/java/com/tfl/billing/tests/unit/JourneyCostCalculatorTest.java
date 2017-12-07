package com.tfl.billing.tests.unit;

import com.tfl.billing.Journey;
import com.tfl.billing.JourneyCostCalculator;
import com.tfl.billing.JourneyEnd;
import com.tfl.billing.JourneyStart;
import com.tfl.billing.utils.ControllableClock;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

/**
 * Created by cosmi_owugxv5 on 12/4/2017.
 */
public class JourneyCostCalculatorTest {

    private UUID cardId;
    private UUID readerId;
    private ControllableClock clock;
    private JourneyCostCalculator journeyCostCalculator;
    private List<Journey> journeys;
    private BigDecimal roundedLongPeak;
    private BigDecimal roundedShortPeak;
    private BigDecimal roundedLongOffPeak;
    private BigDecimal roundedShortOffPeak;
    private BigDecimal roundedPeakLimit;
    private BigDecimal roundedOffPeakLimit;

    @Before
    public void setUp()
    {
        clock = new ControllableClock();
        journeyCostCalculator= new JourneyCostCalculator();
        journeys= new ArrayList<Journey>();
        cardId=UUID.randomUUID();
        roundedLongPeak=journeyCostCalculator.getRoundedLongPeak();
        roundedShortPeak=journeyCostCalculator.getRoundedShortPeak();
        roundedLongOffPeak=journeyCostCalculator.getRoundedLongOffPeak();
        roundedShortOffPeak=journeyCostCalculator.getRoundedShortOffPeak();
        roundedPeakLimit=journeyCostCalculator.getPeakLimit();
        roundedOffPeakLimit=journeyCostCalculator.getOffPeakLimit();
    }

    @Test
    public void testJourneyCostLongPeak()
    {
        setUpLongPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedLongPeak);
    }

    @Test
    public void testJourneyCostShortPeak()
    {
        setUpShortPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedShortPeak);
    }

    @Test
    public void testJourneyCostLongOffPeak()
    {
        setUpLongOffPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedLongOffPeak);
    }

    @Test
    public void testJourneyCostShortOffPeak()
    {
        setUpShortOffPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedShortOffPeak);
    }

    @Test
    public void testTwoJourneyBothShort()
    {
        setUpShortPeak();
        setUpShortOffPeak();
        assertThat(journeys.size(), is(2));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedShortOffPeak.add(roundedShortPeak));
    }

    @Test
    public void testTwoJourneyBothLong()
    {
        setUpLongPeak();
        setUpLongOffPeak();
        assertThat(journeys.size(), is(2));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedLongOffPeak.add(roundedLongPeak));
    }

    @Test
    public void testOneShortPeakOneLongOffPeak()
    {
        setUpShortPeak();
        setUpLongOffPeak();
        assertThat(journeys.size(), is(2));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedLongOffPeak.add(roundedShortPeak));
    }

    @Test
    public void testOneLongPeakOneShortOffPeak()
    {
        setUpLongPeak();
        setUpShortOffPeak();
        assertThat(journeys.size(), is(2));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedShortOffPeak.add(roundedLongPeak));
    }
    @Test
    public void testPeakLimitCap()
    {
        setUpLongPeak();
        setUpLongPeak();
        setUpLongOffPeak();
        setUpLongPeak();
        assertThat(journeys.size(), is(4));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedPeakLimit);
    }

    @Test
    public void testOffPeakLimitCap()
    {
        setUpLongOffPeak();
        setUpLongOffPeak();
        setUpLongOffPeak();
        setUpLongOffPeak();
        assertThat(journeys.size(), is(4));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedOffPeakLimit);
    }

    private void setUpLongPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(4, 0, 0);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(6,11,32);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }

    private void setUpShortPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(6, 0, 0);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(6,11,32);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }

    private void setUpLongOffPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(2, 12, 5);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(2,37,5);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }

    private void setUpShortOffPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(2, 12, 0);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(2,27,38);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }
}
