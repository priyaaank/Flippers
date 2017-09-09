package org.flippers.agent;

import org.flippers.messages.DataMessage;

public interface ReceivedMessageHandler {

    void handle(DataMessage packet);

}
