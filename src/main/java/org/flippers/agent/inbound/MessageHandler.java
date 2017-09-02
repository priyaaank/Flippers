package org.flippers.agent.inbound;

import org.flippers.messages.DataMessage;

public interface MessageHandler {

    void handle(DataMessage packet);

}
