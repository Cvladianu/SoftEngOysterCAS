package com.tfl.billing;

import com.oyster.OysterCard;
import com.tfl.billing.Adaptors.CustomersDatabase;
import com.tfl.billing.Utils.ControllableClock;
import com.tfl.billing.Utils.ControllablePaymentSystem;
import com.tfl.external.Customer;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
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

    @Before
    public void setUp()
    {
        clock = new ControllableClock();
        journeyCostCalculator= new JourneyCostCalculator();
        journeys= new ArrayList<Journey>();
        cardId=UUID.randomUUID();
        roundedOffPeak= journeyCostCalculator.getRoundedOffPeak();
        roundedPeak= journeyCostCalculator.getRoundedPeak();
    }
    @Test
    public void TestJourneyCostPeak()
    {
        setUpPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedPeak);
    }

    @Test
    public void TestJourneyCostOffPeak()
    {
        setUpOffPeak();
        assertThat(journeys.size(), is(1)) ;
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedOffPeak);
    }

    @Test
    public void TestTwoJourneyBothCosts()
    {
        setUpPeak();
        setUpOffPeak();
        assertThat(journeys.size(), is(2));
        assertEquals(journeyCostCalculator.customerTotalFor(journeys), roundedOffPeak.add(roundedPeak));
    }

    private void setUpPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(4, 0, 0);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(6,11,32);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }

    private void setUpOffPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(2, 12, 0);
        JourneyStart start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(3,11,32);
        JourneyEnd end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }
}