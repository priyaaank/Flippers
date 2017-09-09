package org.flippers.commands;

import org.flippers.agent.MessageSender;
import org.flippers.agent.MessageType;
import org.flippers.config.Config;
import org.flippers.messages.DataMessage;

public class PingHandler implements Command {

    private MessageSender sender;
    private Config config;

    public PingHandler(MessageSender sender, Config config) {
        this.sender = sender;
        this.config = config;
    }

    @Override
    public void handle(DataMessage receivedMessage) {
        DataMessage ackMessage = new DataMessage(
                receivedMessage.getSequenceNumber(),
                receivedMessage.getInetAddress(),
                MessageType.ACK,
                receivedMessage.getSourcePort(),
                config.getListenPort()
        );
        this.sender.send(ackMessage);
    }
}
