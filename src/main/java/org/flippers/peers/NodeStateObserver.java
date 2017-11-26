package org.flippers.peers;

public interface NodeStateObserver {

    void markJoined(PeerNode peerNode);

    void markPingAwaited(PeerNode peerNode);

    void markIndirectPingAwaited(PeerNode peerNode);

    void markAlive(PeerNode peerNode);

    void markDead(PeerNode peerNode);

    void markExited(PeerNode peerNode);

    void markFailureSuspected(PeerNode peerNode);
}
