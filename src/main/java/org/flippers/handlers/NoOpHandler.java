package org.flippers.handlers;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoOpHandler implements Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoOpHandler.class);

    @Override
    public void handle(DataMessage dataMessage) {
        LOGGER.debug("Do nothing!");
    }
}
