package org.flippers.peers;

public interface NodeStateObserver {

    void stateUpdated(PeerNode updatedNode);

}
