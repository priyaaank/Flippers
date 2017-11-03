package org.flippers.handlers;

import org.flippers.messages.DataMessage;

public interface HandlerExecutor {

    void executeHandler(DataMessage packet);

}
