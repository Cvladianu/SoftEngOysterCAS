package com.tfl.billing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class JourneyEndTest {
    UUID cardId= UUID.randomUUID();
    UUID readerId= UUID.randomUUID();
    JourneyEnd journeyEnd = new JourneyEnd(cardId, readerId);

    @Test
    public void createdJourneyEnd()
    {
        assertThat(journeyEnd.cardId(), is(cardId));
        assertThat(journeyEnd.readerId(), is(readerId));
    }
}
