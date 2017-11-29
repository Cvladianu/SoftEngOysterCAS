package com.tfl.billing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class TravelTrackerTest {
    TravelTracker travelTracker= new TravelTracker();
    UUID cardId;
    UUID readerId;

    @Test (expected = UnknownOysterCardException.class)
    public void TestCardScannedException()
    {
        cardId=UUID.fromString("267b3378-678d-4da7-825e-3252982d48ab");
        readerId=UUID.randomUUID();

        travelTracker.cardScanned(cardId, readerId);
    }

    @Test
    public void TestCardScannedExceptionMessage()
    {
        cardId=UUID.fromString("267b3378-678d-4da7-825e-3552982d48ab");
        readerId=UUID.randomUUID();

        try {
            travelTracker.cardScanned(cardId, readerId);
        }catch(UnknownOysterCardException e) {
            String s="Oyster Card does not correspond to a known customer. Id: " + cardId;
            assertEquals(e.getMessage(), s);
        };
    }
}
