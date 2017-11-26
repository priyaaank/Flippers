package org.flippers.dissemination;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;

import static org.flippers.dissemination.EventType.*;

public class EventGenerator implements NodeStateObserver {

    private EventLog eventLog;

    public EventGenerator(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    @Override
    public void markPingAwaited(PeerNode peerNode) {
        //Do nothing
    }

    @Override
    public void markIndirectPingAwaited(PeerNode peerNode) {
        //Do nothing
    }

    @Override
    public void markAlive(PeerNode peerNode) {
        //In case of alive, it is important to know the previous state, since only if state changes
        //we need to disseminate the information about node being alived
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), ALIVE));
    }

    @Override
    public void markJoined(PeerNode peerNode) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), JOINED));
    }

    @Override
    public void markDead(PeerNode peerNode) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), DEAD));
    }

    @Override
    public void markExited(PeerNode peerNode) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), EXITED));
    }

    @Override
    public void markFailureSuspected(PeerNode peerNode) {
        this.eventLog.enqueue(new Event(peerNode.getIpAddress().getHostAddress(), SUSPECT));
    }

}
