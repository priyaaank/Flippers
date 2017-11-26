package org.flippers.peers.states;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;

import java.util.List;

public class Joined implements NodeState {
    @Override
    public void publishStateTransition(PeerNode node, List<NodeStateObserver> observers) {
        observers.forEach(o -> o.markJoined(node));
        node.markAlive();
    }
}
