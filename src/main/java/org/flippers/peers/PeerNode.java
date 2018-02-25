package org.flippers.peers;

import org.flippers.peers.states.NodeState;
import org.flippers.peers.states.NodeStateFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.apache.commons.lang3.StringUtils.isEmpty;

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

    public boolean isInteractionInitiatedWithinMilliSeconds(long milliSecondsAgo) {
        return this.interaction.isInteractionInitiatedWithinMilliSeconds(milliSecondsAgo);
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

    @Override
    public String toString() {
        return "PeerNode{" +
                "ipAddress=" + ipAddress +
                ", port=" + port +
                ", state=" + state +
                '}';
    }

    private void transitionState(NodeState prevState, NodeState toState) {
        this.state = toState;
        this.state.publishStateTransition(this, observers, prevState);
    }

    public static PeerNode nodeFor(String ipAddress, String port) throws UnknownHostException {
        if (isEmpty(port)) throw new UnknownHostException("Port is missing for the host " + ipAddress);
        return new PeerNode(InetAddress.getByName(ipAddress), Integer.valueOf(port));
    }
}
