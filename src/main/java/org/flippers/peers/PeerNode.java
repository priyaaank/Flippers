package org.flippers.peers;

import org.flippers.peers.states.NodeState;
import org.flippers.peers.states.NodeStateFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class PeerNode {

    private InetAddress ipAddress;
    private Integer port;
    private NodeState state;
    private List<NodeStateObserver> observers;

    public PeerNode(InetAddress ipAddress, Integer port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.observers = new ArrayList<>();
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
        this.state = NodeStateFactory.getInstance().awaitingAckState();
        this.state.publishStateTransition(this, observers);
    }

    public void initJoining() {
        this.state = NodeStateFactory.getInstance().joinedState();
        this.state.publishStateTransition(this, observers);
    }

    public void markAlive() {
        this.state = NodeStateFactory.getInstance().aliveState();
        this.state.publishStateTransition(this, observers);
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

}
