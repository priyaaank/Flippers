package org.flippers.peers;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class NodeInteraction {

    private final ZonedDateTime interactionInitiationAt;

    public NodeInteraction() {
        this(ZonedDateTime.now());
    }

    public NodeInteraction(ZonedDateTime interactionInitiationAt) {
        this.interactionInitiationAt = interactionInitiationAt;
    }

    public Boolean isInteractionInitiatedWithinMilliSeconds(Long secondsAgo) {
        return ChronoUnit.MILLIS.between(this.interactionInitiationAt, ZonedDateTime.now()) <= secondsAgo;
    }
}
