package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.messages.MessageType;
import org.flippers.config.Config;
import org.flippers.messages.DataMessage;

public class PingHandler implements Handler {

    private MessageSender sender;
    private Config config;

    public PingHandler(MessageSender sender, Config config) {
        this.sender = sender;
        this.config = config;
    }

    @Override
    public void handle(DataMessage receivedMessage) {
        sendAckResponse(receivedMessage);
    }

    private void sendAckResponse(DataMessage receivedPingMsg) {
        this.sender.send(buildAckMsgFromPingReq(receivedPingMsg));
    }

    private DataMessage buildAckMsgFromPingReq(DataMessage receivedMessage) {
        return new DataMessage(
                receivedMessage.getSequenceNumber(),
                receivedMessage.getSourceAddress(),
                MessageType.ACK,
                receivedMessage.getSourcePort(),
                config.getListenPort()
        );
    }
}
