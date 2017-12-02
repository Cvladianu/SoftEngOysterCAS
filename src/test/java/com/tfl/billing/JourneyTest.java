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
    private Cloackable clock;
    private JourneyStart start;
    private JourneyEnd end;
    private Journey journey;
    private RandomVals randomVals= new RandomVals();

    @Before
    public void setUp()
    {
        UUID cardId= UUID.randomUUID();
        UUID readerIdStart= UUID.randomUUID();
        UUID readerIdEnd= UUID.randomUUID();
        clock = new SystemClock();
    }

    @Test
    public void TestZeroSecondsJourney()
    {
        start = new JourneyStart(cardId, readerIdStart);
        end= new JourneyEnd(cardId, readerIdEnd);
        journey = new Journey(start,end);
        assertThat(journey.durationSeconds(), is(0));
    }

    @Test
    public void TestTwoSecondsJourney()
    {

        start = new JourneyStart(cardId, readerIdStart);
        try {
            Thread.sleep(2000);                 //1000 milliseconds Cloackable one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        end= new JourneyEnd(cardId, readerIdEnd);
        journey = new Journey(start,end);

        assertThat(journey.durationSeconds(), is(2));
    }

    @Test
    public void TestDifRandom()
    {
        start=new JourneyStart(cardId, readerIdStart);
        long randomMili=randomVals.getRandomMilis();
        long d= randomMili+start.time();
        long dSeconds=randomMili/1000;

        clock=context.mock(Cloackable.class);

        context.checking(new Expectations(){{
            oneOf(clock).currentTimeMillis(); will(returnValue(d));
    }});
        end=new JourneyEnd(cardId, readerIdEnd, clock);
        journey= new Journey(start,end);

        assertThat((long) journey.durationSeconds(), is(dSeconds));
        context.assertIsSatisfied();
    }
}
