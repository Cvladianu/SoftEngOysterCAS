package com.tfl.billing.tests.unit;

import com.oyster.OysterCard;
import com.tfl.billing.*;
import com.tfl.billing.utils.ControllableClock;
import com.tfl.billing.utils.ControllablePaymentSystem;
import com.tfl.external.Customer;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 12/5/2017.
 */
public class ChargerTest {

    private UUID cardId;
    private UUID readerId;
    private ControllableClock clock;
    private JourneyCostCalculator journeyCostCalculator;
    private List<Journey> journeys;
    private JourneyStart start;
    private JourneyEnd end;
    private CustomerCharger customerCharger;
    private Customer customer;
    private ControllablePaymentSystem paymentSystem;

    @Before
    public void setUp()
    {
        clock = new ControllableClock();
        journeyCostCalculator= new JourneyCostCalculator();
        journeys= new ArrayList<Journey>();
        cardId=UUID.randomUUID();
        customer= new Customer("Fred Bloggs", new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d"));
        customerCharger = new CustomerCharger();
        paymentSystem = new ControllablePaymentSystem();

        readerId= UUID.randomUUID();
        clock.setTime(4, 0, 0);
        start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(6,11,32);
        end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));
    }

    @Test
    public void testChargeAmountOneJourneyLongPeak()
    {
        customerCharger.charge(customer, journeys, paymentSystem);

        assertEquals(customer, paymentSystem.getCustomer());
        assertEquals(journeys, paymentSystem.getJourneys());
        assertEquals(journeyCostCalculator.getRoundedLongPeak(), paymentSystem.getTotalBill());
    }

    @Test
    public void testChargeAmountTwoJourneysLongPeakAndShortOffPeak()
    {
        readerId=UUID.randomUUID();
        clock.setTime(2, 12, 0);
        start = new JourneyStart(cardId, readerId, clock);
        readerId=UUID.randomUUID();
        clock.setTime(2,15,32);
        end = new JourneyEnd(cardId, readerId, clock);
        journeys.add(new Journey(start,end));

        customerCharger.charge(customer, journeys, paymentSystem);

        assertEquals(customer, paymentSystem.getCustomer());
        assertEquals(journeys, paymentSystem.getJourneys());
        assertEquals(journeyCostCalculator.getRoundedLongPeak().add(journeyCostCalculator.getRoundedShortOffPeak()), paymentSystem.getTotalBill());
    }
}
