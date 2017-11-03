package org.flippers.handlers;

import org.flippers.messages.DataMessage;

public interface Handler {

    void handle(DataMessage dataMessage);

}
