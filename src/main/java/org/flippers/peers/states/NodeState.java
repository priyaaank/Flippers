package org.flippers.peers.states;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;

import java.util.List;

public enum NodeState {

    ALIVE((node, observers, fromState) -> observers.forEach(o -> o.markAlive(node, fromState))),

    AWAITING_ACK((node, observers, fromState) -> observers.forEach(o -> o.markPingAwaited(node, fromState))),

    AWAITING_INDIRECT_ACK((node, observers, fromState) -> observers.forEach(o -> o.markIndirectPingAwaited(node, fromState))),

    DEAD((node, observers, fromState) -> observers.forEach(o -> o.markDead(node, fromState))),

    EXITED((node, observers, fromState) -> observers.forEach(o -> o.markExited(node, fromState))),

    FAILURE_SUSPECTED((node, observers, fromState) -> observers.forEach(o -> o.markFailureSuspected(node, fromState))),

    JOINED((node, observers, fromState) -> {
        observers.forEach(o -> o.markJoined(node, fromState));
        node.markAlive();
    });

    private TransitionAction transitionAction;

    NodeState(TransitionAction transitionAction) {
        this.transitionAction = transitionAction;
    }

    public void transitionState(PeerNode node, List<NodeStateObserver> observers, NodeState fromState) {
        this.transitionAction.transitionState(node, observers, fromState);
    }

    private interface TransitionAction {
        void transitionState(PeerNode node, List<NodeStateObserver> observers, NodeState fromState);
    }
}
