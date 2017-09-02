package org.flippers.commands;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoOp implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoOp.class);

    @Override
    public void handle(DataMessage dataMessage) {
        LOGGER.debug("Do nothing!");
    }
}
