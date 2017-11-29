package com.tfl.billing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
}
