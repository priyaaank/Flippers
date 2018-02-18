package org.flippers.messages;

import org.flippers.config.Config;
import org.flippers.peers.PeerNode;

import static org.flippers.config.Config.DefaultValues.DEFAULT_LISTEN_PORT;
import static org.flippers.config.Config.KeyNames.LISTEN_PORT;

public class MessageCreator {

    private final SequenceNumberGenerator sequenceNumberGenerator;
    private Config config;

    public MessageCreator(Config config) {
        this.config = config;
        this.sequenceNumberGenerator = new SequenceNumberGenerator();
    }

    public DataMessage ackResponseForPingMsg(DataMessage receivedMessage) {
        return new DataMessage(
                receivedMessage.getSequenceNumber(),
                receivedMessage.getSourceAddress(),
                MessageType.ACK,
                receivedMessage.getSourcePort(),
                config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT)
        );
    }

    public DataMessage craftPingMsg(PeerNode node) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                node.getIpAddress(),
                MessageType.PING,
                node.getPort(),
                config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT)
        );
    }

    public DataMessage craftIndirectPingMsg(PeerNode node) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                node.getIpAddress(),
                MessageType.PING_REQ,
                node.getPort(),
                config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT)
        );
    }

    public DataMessage ackResponseForJoinMsg(DataMessage joinMessage) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                joinMessage.getSourceAddress(),
                MessageType.ACK_JOIN,
                joinMessage.getSourcePort(),
                config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT)
        );
    }
}
