package org.flippers.peers;

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
        this.state = NodeState.RUNNING;
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
        this.state = NodeState.PING_INITIATED;
        this.observers.forEach(o -> o.stateUpdated(this));
    }

    enum NodeState {
        RUNNING,
        PING_INITIATED,
        INDIRECT_PING_INITIATED,
        FAILURE_SUSPECTED,
        FAILED,
        EXITED
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
