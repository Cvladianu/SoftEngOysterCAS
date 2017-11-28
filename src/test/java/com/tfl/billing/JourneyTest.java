package com.tfl.billing;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by cosmi_owugxv5 on 11/20/2017.
 */
public class JourneyTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID cardId;
    private UUID readerIdStart;
    private UUID readerIdEnd;
    private ClockInterface clock;
    private JourneyStart start;
    private JourneyEnd end;
    private Journey journey;

    @Before
    public void setUp()
    {
        UUID cardId= UUID.randomUUID();
        UUID readerIdStart= UUID.randomUUID();
        UUID readerIdEnd= UUID.randomUUID();
        clock = new SystemClock();
    }

    @Test
    public void TestDifZeroNoMock()
    {
        start = new JourneyStart(cardId, readerIdStart);
        end= new JourneyEnd(cardId, readerIdEnd);
        journey = new Journey(start,end);
        assertThat(journey.durationSeconds(), is(0));
    }

    @Test
    public void TestDifTwoNoMock()
    {

        start = new JourneyStart(cardId, readerIdStart);
        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        end= new JourneyEnd(cardId, readerIdEnd);
        journey = new Journey(start,end);

        assertThat(journey.durationSeconds(), is(2));
    }
}
