package org.flippers.peers.states;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;

import java.util.List;

public class Exited implements NodeState {
    @Override
    public void publishStateTransition(PeerNode node, List<NodeStateObserver> observers, NodeState fromState) {
        observers.forEach(o -> o.markExited(node, fromState));
    }
}
