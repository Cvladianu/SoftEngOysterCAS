package com.tfl.billing.unitTests;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.tfl.billing.JourneyEnd;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class JourneyEndTest {
    private UUID cardId= UUID.randomUUID();
    private UUID readerId= UUID.randomUUID();
    private JourneyEnd journeyEnd = new JourneyEnd(cardId, readerId);

    @Test
    public void createdJourneyEnd()
    {
        assertThat(journeyEnd.cardId(), is(cardId));
        assertThat(journeyEnd.readerId(), is(readerId));
    }
}
