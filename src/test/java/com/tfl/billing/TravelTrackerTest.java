package com.tfl.billing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

import java.util.*;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class TravelTrackerTest {

    JUnitRuleMockery context = new JUnitRuleMockery();

    TravelTracker travelTracker;
    UUID cardId;
    UUID readerId;

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
    public void keepsARecordOfTheTravellingCustomers()
    {
        MockableDatabase md = context.mock(MockableDatabase.class);

        cardId=UUID.fromString("267b3378-678d-4da7-825e-3552982d48ab");
        readerId=UUID.randomUUID();

        List<JourneyEvent> eventLog = new ArrayList<>();
        Set<UUID> currentlyTravelling = new HashSet<>();

        travelTracker= new TravelTracker(eventLog, currentlyTravelling, md);

        context.checking(new Expectations() { {
            oneOf(md).isRegisteredId(cardId); will(returnValue(true));
        }});

       travelTracker.cardScanned(cardId, readerId);
       assertTrue(currentlyTravelling.contains(cardId)) ;
    }

}
