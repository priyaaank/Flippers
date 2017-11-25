package org.flippers.peers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class NodeInteraction {

    private static Logger LOGGER = LoggerFactory.getLogger(NodeInteraction.class);

    private final ZonedDateTime interactionInitiationAt;

    public NodeInteraction() {
        this(ZonedDateTime.now());
    }

    public NodeInteraction(ZonedDateTime interactionInitiationAt) {
        this.interactionInitiationAt = interactionInitiationAt;
    }

    public Boolean isInteractionInitiatedWithinMilliSeconds(Long milliSecondsAgo) {
        Long milliSecondsBetween = ChronoUnit.MILLIS.between(this.interactionInitiationAt, ZonedDateTime.now());
        LOGGER.debug("Milliseconds between interaction initiation and time right now is {}", milliSecondsAgo);
        return milliSecondsBetween <= milliSecondsAgo;
    }
}
