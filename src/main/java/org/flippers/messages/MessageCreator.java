package org.flippers.messages;

import org.flippers.config.Config;
import org.flippers.peers.PeerNode;

public class MessageCreator {

    private final SequenceNumberGenerator sequenceNumberGenerator;
    private Config config;

    public MessageCreator(Config config) {
        this.config = config;
        this.sequenceNumberGenerator = new SequenceNumberGenerator();
    }

    public DataMessage ackResponseForReceivedMsg(DataMessage receivedMessage) {
        return new DataMessage(
                receivedMessage.getSequenceNumber(),
                receivedMessage.getSourceAddress(),
                MessageType.ACK,
                receivedMessage.getSourcePort(),
                config.getListenPort()
        );
    }

    public DataMessage craftPingMsg(PeerNode node) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                node.getIpAddress(),
                MessageType.PING,
                node.getPort(),
                config.getListenPort()
        );
    }

    public DataMessage craftIndirectPingMsg(PeerNode node) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                node.getIpAddress(),
                MessageType.PING_REQ,
                node.getPort(),
                config.getListenPort()
        );
    }
}
