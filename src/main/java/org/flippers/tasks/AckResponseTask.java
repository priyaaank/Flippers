package org.flippers.tasks;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;

public class AckResponseTask implements Task {

    private static Logger LOGGER = LoggerFactory.getLogger(AckResponseTask.class);
    private DataMessage message;

    public AckResponseTask(DataMessage message) {
        this.message = message;
    }

    @Override
    public void execute() {
        try {
            new DatagramSocket().send(message.toDatagramPacket());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
