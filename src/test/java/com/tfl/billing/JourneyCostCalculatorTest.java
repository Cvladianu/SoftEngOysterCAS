package com.tfl.billing;

import com.tfl.billing.Utils.ControllableClock;
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
    private BigDecimal roundedOffPeak;
    private BigDecimal roundedPeak;
    private BigDecimal roundedLongPeak;
    private BigDecimal roundedShortPeak;
    private BigDecimal roundedLongOffPeak;
    private BigDecimal roundedShortOffPeak;

    @Before
    public void setUp()
    {
        clock = new ControllableClock();
        journeyCostCalculator= new JourneyCostCalculator();
        journeys= new ArrayList<Journey>();
        cardId=UUID.randomUUID();
        roundedOffPeak= journeyCostCalculator.getRoundedOffPeak();
        roundedPeak= journeyCostCalculator.getRoundedPeak();
        roundedLongPeak=journeyCostCalculator.getRoundedLongPeak();
        roundedShortPeak=journeyCostCalculator.getRoundedShortPeak();
        roundedLongOffPeak=journeyCostCalculator.getRoundedLongOffPeak();
        roundedShortOffPeak=journeyCostCalculator.getRoundedShortOffPeak();
    }
    @Test
    public void TestJourneyCostPeak()
    {
        setUpShortPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedPeak);
    }

    @Test
    public void TestJourneyCostLongPeak()
    {
        setUpLongPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedLongPeak);
    }

    @Test
    public void TestJourneyCostShortPeak()
    {
        setUpLongPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedShortPeak);
    }

    @Test
    public void TestJourneyCostLongOffPeak()
    {
        setUpLongOffPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedLongOffPeak);
    }

    @Test
    public void TestJourneyCostShortOffPeak()
    {
        setUpLongOffPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedShortOffPeak);
    }

    @Test
    public void TestJourneyCostOffPeak()
    {
        setUpShortOffPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedOffPeak);
    }

    @Test
    public void TestTwoJourneyBothCosts()
    {
        setUpShortPeak();
        setUpShortOffPeak();
        assertThat(journeys.size(), is(2));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedOffPeak.add(roundedPeak));
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
        clock.setTime(2, 12, 0);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(3,11,32);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }

    private void setUpShortOffPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(2, 12, 0);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(2,26,32);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }
}
