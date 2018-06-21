package org.flippers.dissemination;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;
import org.flippers.peers.states.NodeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.flippers.dissemination.EventType.*;

public class EventGenerator implements NodeStateObserver {

    private static Logger LOGGER = LoggerFactory.getLogger(EventGenerator.class);
    private EventLog eventLog;

    public EventGenerator(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    @Override
    public void markPingAwaited(PeerNode peerNode, NodeState fromState) {
        //Do nothing
    }

    @Override
    public void markIndirectPingAwaited(PeerNode peerNode, NodeState fromState) {
        //Do nothing
    }

    @Override
    public void markAlive(PeerNode peerNode, NodeState fromState) {
        if (fromState == NodeState.AWAITING_ACK || fromState == NodeState.AWAITING_INDIRECT_ACK || fromState == NodeState.JOINED) {
            LOGGER.debug("Node {} does not seem to be dead. Ignoring its alive status", peerNode);
            return;
        }

        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), ALIVE));
    }

    @Override
    public void markJoined(PeerNode peerNode, NodeState fromState) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), JOINED));
    }

    @Override
    public void markDead(PeerNode peerNode, NodeState fromState) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), DEAD));
    }

    @Override
    public void markExited(PeerNode peerNode, NodeState fromState) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), EXITED));
    }

    @Override
    public void markFailureSuspected(PeerNode peerNode, NodeState fromState) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), SUSPECT));
    }

}
