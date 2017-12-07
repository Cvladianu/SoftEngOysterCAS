package com.tfl.billing.tests.system;

import com.oyster.OysterCard;
import com.tfl.billing.*;
import com.tfl.billing.adaptors.CustomersDatabase;
import com.tfl.billing.utils.ControllableClock;
import com.tfl.billing.utils.ControllablePaymentSystem;
import com.tfl.external.Customer;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by cosmi_owugxv5 on 12/6/2017.
 */
public class TravelTrackerSysTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private CustomersDatabase md;
    private UUID cardId;
    private UUID readerId;
    private List<Customer> customers;
    private TravelTracker travelTracker;
    private List<JourneyEvent> eventLog;
    private JourneyStart start;
    private JourneyEnd end;
    private Set<UUID> currentlyTravelling;
    private ControllableClock clock;
    private ControllablePaymentSystem paymentSystem;
    private JourneyCostCalculator journeyCostCalculator;

    @Before
    public void setUp()
    {
        clock = new ControllableClock();
        paymentSystem = new ControllablePaymentSystem();
        md = context.mock(CustomersDatabase.class);
        eventLog = new ArrayList<>();
        currentlyTravelling = new HashSet<>();
        journeyCostCalculator= new JourneyCostCalculator();
        customers= new ArrayList<Customer>();
        cardId=UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

    }

    @Test
    public void testJourneyCostLongPeak()
    {
        setUpLongPeak();

        customers.add(new Customer("Fred Bloggs", new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d")));
        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md, paymentSystem);

        context.checking(new Expectations() { {
            oneOf(md).getCustomers(); will(returnValue(customers));
        }});

        travelTracker.chargeAccounts();
        assertEquals( journeyCostCalculator.getRoundedLongPeak(), paymentSystem.getTotal());
        context.assertIsSatisfied();
    }

    @Test
    public void testJourneyCostLongOffPeak()
    {
        setUpLongOffPeak();
        //initialise customerList

        customers.add(new Customer("Fred Bloggs", new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d")));
        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md, paymentSystem);

        context.checking(new Expectations() { {
            oneOf(md).getCustomers(); will(returnValue(customers));
        }});

        travelTracker.chargeAccounts();
        assertEquals(journeyCostCalculator.getRoundedLongOffPeak(), paymentSystem.getTotal());
        context.assertIsSatisfied();
    }

    @Test
    public void testTwoJourneyBothLong()
    {
        setUpLongPeak();
        setUpLongOffPeak();

        customers.add(new Customer("Fred Bloggs", new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d")));
        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md, paymentSystem);

        context.checking(new Expectations() { {
            oneOf(md).getCustomers(); will(returnValue(customers));
        }});

        travelTracker.chargeAccounts();
        assertEquals(journeyCostCalculator.getRoundedLongOffPeak().add(journeyCostCalculator.getRoundedLongPeak()), paymentSystem.getTotal());
        context.assertIsSatisfied();
    }

    private void setUpLongPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(4, 0, 0);
        start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(6,11,32);
        end = new JourneyEnd(cardId, readerId, clock);
        eventLog.add(start);
        eventLog.add(end);
    }

    private void setUpLongOffPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(2, 12, 5);
        start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(2,37,5);
        end = new JourneyEnd(cardId, readerId, clock);
        eventLog.add(start);
        eventLog.add(end);
    }

    //commented as currently not used, but might be useful in the future
    /*private void setUpShortPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(6, 0, 0);
        start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(6,11,32);
        end = new JourneyEnd(cardId, readerId, clock);
        eventLog.add(start);
        eventLog.add(end);
    }

    private void setUpShortOffPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(2, 12, 0);
        start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(2,27,38);
        end = new JourneyEnd(cardId, readerId, clock);
        eventLog.add(start);
        eventLog.add(end);
    }*/
}
