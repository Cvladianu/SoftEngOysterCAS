package com.tfl.billing.unitTests;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.tfl.billing.JourneyStart;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class JourneyStartTest {
    private UUID cardId= UUID.randomUUID();
    private UUID readerId= UUID.randomUUID();
    private JourneyStart journeyStart = new JourneyStart(cardId, readerId);

    @Test
    public void createdJourneyStart()
    {
        assertThat(journeyStart.cardId(), is(cardId));
        assertThat(journeyStart.readerId(), is(readerId));
    }
}
