package org.flippers.peers;

import org.flippers.peers.states.NodeState;

public interface NodeStateObserver {

    void markJoined(PeerNode peerNode, NodeState fromState);

    void markPingAwaited(PeerNode peerNode, NodeState fromState);

    void markIndirectPingAwaited(PeerNode peerNode, NodeState fromState);

    void markAlive(PeerNode peerNode, NodeState fromState);

    void markDead(PeerNode peerNode, NodeState fromState);

    void markExited(PeerNode peerNode, NodeState fromState);

    void markFailureSuspected(PeerNode peerNode, NodeState fromState);
}
