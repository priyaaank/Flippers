package org.flippers.peers.states;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;

import java.util.List;

public interface NodeState {

    void publishStateTransition(PeerNode node, List<NodeStateObserver> observers);

}
