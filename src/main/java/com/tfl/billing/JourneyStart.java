package com.tfl.billing;

import java.util.UUID;

public class JourneyStart extends JourneyEvent {
    public JourneyStart(UUID cardId, UUID readerId) {
        super(cardId, readerId);
    }

    public JourneyStart(UUID cardId, UUID readerId, ClockI clock) {
        super(cardId, readerId, clock);
    }
}
