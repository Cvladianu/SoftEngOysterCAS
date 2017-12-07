package com.tfl.billing.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.tfl.billing.adaptors.AdaptorDatabase;
import com.tfl.billing.adaptors.AdaptorPaymentSystem;
import com.tfl.billing.adaptors.CustomersDatabase;
import com.tfl.billing.JourneyEvent;
import com.tfl.billing.TravelTracker;
import com.tfl.billing.UnknownOysterCardException;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.*;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class TravelTrackerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID cardId1;
    private UUID readerId;
    private TravelTracker travelTracker;
    private List<JourneyEvent> eventLog;
    private Set<UUID> currentlyTravelling;
    private CustomersDatabase md;

    @Before
    public void setUp()
    {
        cardId1 =UUID.fromString("267b3378-678d-4da7-825e-3552982d48ab");
        readerId=UUID.randomUUID();
        eventLog = new ArrayList<>();
        currentlyTravelling = new HashSet<>();
        md = context.mock(CustomersDatabase.class);
    }

    @Test (expected = UnknownOysterCardException.class)
    public void throwsExceptionIfUnknownCustomerTriesToScanCard()
    {
        travelTracker= new TravelTracker();
        travelTracker.cardScanned(cardId1, readerId);
    }

    @Test
    public void exceptionHasSuggestiveMessage()
    {
        travelTracker= new TravelTracker();

        try {
            travelTracker.cardScanned(cardId1, readerId);
        }catch(UnknownOysterCardException e) {
            String s="Oyster Card does not correspond to a known customer. Id: " + cardId1;
            assertEquals(e.getMessage(), s);
        }
    }

    @Test
    public void keepsTrackOfTravellingCustomer()
    {
        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md, AdaptorPaymentSystem.getInstance());

        context.checking(new Expectations() { {
            oneOf(md).isRegisteredId(cardId1); will(returnValue(true));
        }});

       travelTracker.cardScanned(cardId1, readerId);
       assertTrue(currentlyTravelling.contains(cardId1));
       assertFalse(eventLog.isEmpty());

       context.assertIsSatisfied();
    }

    @Test
    public void keepsTrackOfMoreTravellingCustomers()
    {
        UUID cardId2;
        cardId2=UUID.fromString("89adbd1c-4de6-40e5-98bc-b3315c6873f2");
        readerId=UUID.randomUUID();
        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md, AdaptorPaymentSystem.getInstance());

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
        currentlyTravelling.add(cardId1);
        travelTracker= new TravelTracker(eventLog, currentlyTravelling, AdaptorDatabase.getInstance(), AdaptorPaymentSystem.getInstance());

        travelTracker.cardScanned(cardId1, readerId);
        assertFalse(currentlyTravelling.contains(cardId1)) ;
        assertFalse(eventLog.isEmpty());
    }
}
