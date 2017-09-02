package org.flippers.commands;

import org.flippers.messages.DataMessage;

public interface Command {

    void handle(DataMessage dataMessage);

}
