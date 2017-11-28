package com.tfl.billing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class JourneyTest {
    UUID cardId= UUID.randomUUID();
    UUID readerId= UUID.randomUUID();
    JourneyStart start = new JourneyStart(cardId, readerId);
    JourneyEnd end = new JourneyEnd(cardId, readerId);

    Journey journey = new Journey(start, end);

    @Test
    public void TestDifZero()
    {
        assertThat(journey.durationSeconds(), is(0));
    }

    @Test
    public void TestDifTwo()
    {
        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        end= new JourneyEnd(cardId, readerId);
        journey = new Journey(start,end);

        assertThat(journey.durationSeconds(), is(2));
    }
}
