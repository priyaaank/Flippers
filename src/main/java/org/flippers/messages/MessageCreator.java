package org.flippers.messages;

import org.flippers.config.FileConfig;
import org.flippers.peers.PeerNode;

import static org.flippers.config.Config.DefaultValues.DEFAULT_LISTEN_PORT;
import static org.flippers.config.Config.KeyNames.LISTEN_PORT;

public class MessageCreator {

    private final SequenceNumberGenerator sequenceNumberGenerator;
    private FileConfig config;

    public MessageCreator(FileConfig config) {
        this.config = config;
        this.sequenceNumberGenerator = new SequenceNumberGenerator();
    }

    public DataMessage ackResponseForReceivedMsg(DataMessage receivedMessage) {
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
}
