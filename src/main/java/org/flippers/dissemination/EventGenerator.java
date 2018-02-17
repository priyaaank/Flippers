package org.flippers.dissemination;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;
import org.flippers.peers.states.AwaitingAck;
import org.flippers.peers.states.AwaitingIndirectAck;
import org.flippers.peers.states.Joined;
import org.flippers.peers.states.NodeState;

import static org.flippers.dissemination.EventType.*;

public class EventGenerator implements NodeStateObserver {

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
        if(fromState instanceof AwaitingAck || fromState instanceof AwaitingIndirectAck || fromState instanceof Joined)
            return;

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
