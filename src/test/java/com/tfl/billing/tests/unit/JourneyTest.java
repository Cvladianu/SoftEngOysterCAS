package com.tfl.billing.tests.unit;

import com.tfl.billing.adaptors.SystemClock;
import com.tfl.billing.Clock;
import com.tfl.billing.Journey;
import com.tfl.billing.JourneyEnd;
import com.tfl.billing.JourneyStart;
import com.tfl.billing.utils.ControllableClock;
import com.tfl.billing.utils.RandomVals;
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
    private JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID cardId;
    private UUID readerIdStart;
    private UUID readerIdEnd;
    private Clock clock;
    private JourneyStart start;
    private JourneyEnd end;
    private Journey journey;
    private RandomVals randomVals;

    @Before
    public void setUp()
    {
        cardId= UUID.randomUUID();
        readerIdStart= UUID.randomUUID();
        readerIdEnd= UUID.randomUUID();
        clock = new SystemClock();
        randomVals= new RandomVals();
    }

    @Test
    public void testZeroSecondsJourney()
    {
        start = new JourneyStart(cardId, readerIdStart);
        end= new JourneyEnd(cardId, readerIdEnd);
        journey = new Journey(start,end);
        assertThat(journey.durationSeconds(), is(0));
    }

    @Test
    public void testTwoSecondsJourney()
    {
        start = new JourneyStart(cardId, readerIdStart);
        try {
            Thread.sleep(2000);                 //1000 milliseconds Clock one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        end= new JourneyEnd(cardId, readerIdEnd);
        journey = new Journey(start,end);

        assertThat(journey.durationSeconds(), is(2));
    }

    @Test
    public void testDifRandom()
    {
        start=new JourneyStart(cardId, readerIdStart);
        long randomMili=randomVals.getRandomMilis();
        long d= randomMili+start.time();
        long dSeconds=randomMili/1000;

        clock=context.mock(Clock.class);

        context.checking(new Expectations(){{
            oneOf(clock).currentTimeMillis(); will(returnValue(d));
    }});
        end=new JourneyEnd(cardId, readerIdEnd, clock);
        journey= new Journey(start,end);

        assertThat((long) journey.durationSeconds(), is(dSeconds));
        context.assertIsSatisfied();
    }

    @Test
    public void testJourneyControlClock()
    {
        ControllableClock controllableClock= new ControllableClock();

        controllableClock.setTime(3,4,5);
        start= new JourneyStart(cardId, readerIdStart, controllableClock);
        controllableClock.setTime(4,5,6);
        end=new JourneyEnd(cardId, readerIdEnd, controllableClock);
        journey= new Journey(start, end);

        assertThat(journey.durationSeconds(), is(3661));
    }
}
