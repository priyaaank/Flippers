package org.flippers.messages;

import org.flippers.config.Config;
import org.flippers.peers.PeerNode;

public class MessageCreator {

    private final SequenceNumberGenerator sequenceNumberGenerator;
    private Config config;
    private PeerNode thisNode;

    public MessageCreator(final Config config, final PeerNode thisNode) {
        this.config = config;
        this.thisNode = thisNode;
        this.sequenceNumberGenerator = new SequenceNumberGenerator();
    }

    public DataMessage ackResponseForPingMsg(DataMessage receivedMessage) {
        return new DataMessage(
                receivedMessage.getSequenceNumber(),
                this.thisNode,
                MessageType.ACK,
                receivedMessage.getSourceNode()
        );
    }

    public DataMessage craftPingMsg(PeerNode destinationNode) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                this.thisNode,
                MessageType.PING,
                destinationNode
        );
    }

    public DataMessage craftIndirectPingMsg(PeerNode destinationNode) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                this.thisNode,
                MessageType.PING_REQ,
                destinationNode //Should this be the destination node or suspect node?
        );
    }

    public DataMessage ackResponseForJoinMsg(DataMessage joinMessage) {
        //Eventually modify to include membership list details.
        //This will allow a joinee to retrieve a full membership
        //list
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                this.thisNode,
                MessageType.ACK_JOIN,
                joinMessage.getSourceNode()
        );
    }

    public DataMessage craftJoinMessage(PeerNode seedNode) {
        return new DataMessage(
                sequenceNumberGenerator.uniqSequence(),
                this.thisNode,
                MessageType.JOIN,
                seedNode
        );
    }
}
