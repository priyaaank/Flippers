package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.config.FileConfig;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageCreator;

public class PingHandler implements Handler {

    private MessageSender sender;
    private MessageCreator messageCreator;

    public PingHandler(MessageSender sender, FileConfig config) {
        this.sender = sender;
        this.messageCreator = new MessageCreator(config);
    }

    @Override
    public void handle(DataMessage receivedMessage) {
        sendAckResponse(receivedMessage);
    }

    private void sendAckResponse(DataMessage receivedPingMsg) {
        this.sender.send(messageCreator.ackResponseForPingMsg(receivedPingMsg));
    }

}
