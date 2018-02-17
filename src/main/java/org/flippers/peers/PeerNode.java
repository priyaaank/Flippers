package org.flippers.peers;

import org.flippers.peers.states.NodeState;
import org.flippers.peers.states.NodeStateFactory;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PeerNode {

    private InetAddress ipAddress;
    private Integer port;
    private NodeState state;
    private List<NodeStateObserver> observers;
    private NodeInteraction interaction;

    public PeerNode(InetAddress ipAddress, Integer port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.observers = new CopyOnWriteArrayList<>();
    }

    public void registerObserver(NodeStateObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void deregisterObserver(NodeStateObserver observer) {
        if (observers.contains(observer)) observers.remove(observer);
    }

    public Integer getPort() {
        return port;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void pingInitiated() {
        this.setInteractionInitiated();
        transitionState(this.state, NodeStateFactory.getInstance().awaitingAckState());
    }

    public void indirectPingInitiated() {
        this.setInteractionInitiated();
        transitionState(this.state, NodeStateFactory.getInstance().awaitingIndirectAckState());
    }

    public void initJoining() {
        transitionState(this.state, NodeStateFactory.getInstance().joinedState());
    }

    public void markAlive() {
        transitionState(this.state, NodeStateFactory.getInstance().aliveState());
    }

    public void markSuspect() {
        this.setInteractionInitiated();
        transitionState(this.state, NodeStateFactory.getInstance().failureSuspectedState());
    }

    public void markDead() {
        transitionState(this.state, NodeStateFactory.getInstance().deadState());
    }

    public void markExited() {
        transitionState(this.state, NodeStateFactory.getInstance().exitedState());
    }

    public boolean isInteractionInitiatedWithinMilliSeconds(long secondsAgo) {
        return this.interaction.isInteractionInitiatedWithinMilliSeconds(secondsAgo);
    }

    public void setInteractionInitiated() {
        this.interaction = new NodeInteraction();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeerNode peerNode = (PeerNode) o;

        if (!ipAddress.equals(peerNode.ipAddress)) return false;
        return port.equals(peerNode.port);
    }

    @Override
    public int hashCode() {
        int result = ipAddress.hashCode();
        result = 31 * result + port.hashCode();
        return result;
    }

    private void transitionState(NodeState prevState, NodeState toState) {
        this.state = toState;
        this.state.publishStateTransition(this, observers, prevState);
    }

}
