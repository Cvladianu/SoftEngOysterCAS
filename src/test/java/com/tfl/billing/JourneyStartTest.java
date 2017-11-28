package com.tfl.billing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class JourneyStartTest {
    UUID cardId= UUID.randomUUID();
    UUID readerId= UUID.randomUUID();
    JourneyStart journeyStart = new JourneyStart(cardId, readerId);

    @Test
    public void createdJourneyStart()
    {
        assertThat(journeyStart.cardId(), is(cardId));
        assertThat(journeyStart.readerId(), is(readerId));
    }
}
