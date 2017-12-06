package com.tfl.billing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oyster.OysterCard;
import com.tfl.billing.Adaptors.CustomersDatabase;
import com.tfl.billing.Utils.ControllableClock;
import com.tfl.billing.Utils.ControllablePaymentSystem;
import com.tfl.external.Customer;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class TravelTrackerTest {

    JUnitRuleMockery context = new JUnitRuleMockery();

    TravelTracker travelTracker;
    UUID cardId;
    UUID readerId;
    List<Customer> customers = new ArrayList<Customer>();

    @Test (expected = UnknownOysterCardException.class)
    public void throwsExceptionIfUnknownCustomerTriesToScanCard()
    {
        travelTracker= new TravelTracker();
        cardId=UUID.fromString("267b3378-678d-4da7-825e-3252982d48ab");
        readerId=UUID.randomUUID();

        travelTracker.cardScanned(cardId, readerId);
    }

    @Test
    public void exceptionHasSuggestiveMessage()
    {
        travelTracker= new TravelTracker();
        cardId=UUID.fromString("267b3378-678d-4da7-825e-3552982d48ab");
        readerId=UUID.randomUUID();

        try {
            travelTracker.cardScanned(cardId, readerId);
        }catch(UnknownOysterCardException e) {
            String s="Oyster Card does not correspond to a known customer. Id: " + cardId;
            assertEquals(e.getMessage(), s);
        };
    }

    @Test
    public void keepsTrackOfTravellingCustomer()
    {
        CustomersDatabase md = context.mock(CustomersDatabase.class);

        cardId=UUID.fromString("267b3378-678d-4da7-825e-3552982d48ab");
        readerId=UUID.randomUUID();

        List<JourneyEvent> eventLog = new ArrayList<>();
        Set<UUID> currentlyTravelling = new HashSet<>();

        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md);

        context.checking(new Expectations() { {
            oneOf(md).isRegisteredId(cardId); will(returnValue(true));
        }});

       travelTracker.cardScanned(cardId, readerId);
       assertTrue(currentlyTravelling.contains(cardId));
       assertFalse(eventLog.isEmpty());

       context.assertIsSatisfied();
    }

    @Test
    public void keepsTrackOfMoreTravellingCustomers()
    {
        CustomersDatabase md = context.mock(CustomersDatabase.class);

        UUID cardId1=UUID.fromString("267b3378-678d-4da7-825e-3552982d48ab");
        UUID cardId2=UUID.fromString("89adbd1c-4de6-40e5-98bc-b3315c6873f2");
        readerId=UUID.randomUUID();

        List<JourneyEvent> eventLog = new ArrayList<>();
        Set<UUID> currentlyTravelling = new HashSet<>();

        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md);

        context.checking(new Expectations() { {
            oneOf(md).isRegisteredId(cardId1); will(returnValue(true));
            oneOf(md).isRegisteredId(cardId2); will(returnValue(true));
        }});

        travelTracker.cardScanned(cardId1, readerId);
        readerId=UUID.randomUUID();
        travelTracker.cardScanned(cardId2, readerId);
        assertTrue(currentlyTravelling.contains(cardId2));

        readerId=UUID.randomUUID();
        travelTracker.cardScanned(cardId2, readerId);

        assertTrue(eventLog.size() == 3);
        assertTrue(currentlyTravelling.contains(cardId1));
        assertFalse(currentlyTravelling.contains(cardId2));

        context.assertIsSatisfied();
    }

    @Test
    public void discardsTravelingCustomerAfterSecondScan()
    {
        cardId=UUID.fromString("267b3378-678d-4da7-825e-3552982d48ab");
        readerId=UUID.randomUUID();

        List<JourneyEvent> eventLog = new ArrayList<>();
        Set<UUID> currentlyTravelling = new HashSet<>();
        currentlyTravelling.add(cardId);
        travelTracker= new TravelTracker(eventLog, currentlyTravelling);

        travelTracker.cardScanned(cardId, readerId);
        assertFalse(currentlyTravelling.contains(cardId)) ;
        assertFalse(eventLog.isEmpty());
    }

}
