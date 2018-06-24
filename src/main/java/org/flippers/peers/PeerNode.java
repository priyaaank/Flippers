package org.flippers.peers;

import org.flippers.messages.MessageProtos;
import org.flippers.peers.states.NodeState;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class PeerNode {

    private String nodeId;
    private InetAddress ipAddress;
    private Integer port;
    private NodeState state;
    private NodeInteraction interaction;
    private List<NodeStateObserver> observers;

    public PeerNode(InetAddress ipAddress, Integer port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.observers = new CopyOnWriteArrayList<>();
    }

    public static PeerNode from(MessageProtos.NodeInfo nodeInfo) {
        return null;
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
        transitionState(this.state, NodeState.AWAITING_ACK);
    }

    public void indirectPingInitiated() {
        this.setInteractionInitiated();
        transitionState(this.state, NodeState.AWAITING_INDIRECT_ACK);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void initJoining() {
        transitionState(this.state, NodeState.JOINED);
    }

    public void markAlive() {
        transitionState(this.state, NodeState.ALIVE);
    }

    public void markSuspect() {
        this.setInteractionInitiated();
        transitionState(this.state, NodeState.FAILURE_SUSPECTED);
    }

    public void markDead() {
        transitionState(this.state, NodeState.DEAD);
    }

    public void markExited() {
        transitionState(this.state, NodeState.EXITED);
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
        this.state.transitionState(this, observers, prevState);
    }

    public static PeerNode nodeFor(InetAddress ipAddress, int port) throws UnknownHostException {
        if (port <= -1) throw new UnknownHostException("Port is missing for the host " + ipAddress);
        return new PeerNode(ipAddress, port);
    }

    public static PeerNode nodeFor(InetAddress ipAddress, String port) throws UnknownHostException {
        if (isEmpty(port)) throw new UnknownHostException("Port is missing for the host " + ipAddress);
        return nodeFor(ipAddress, Integer.valueOf(port));
    }

    public MessageProtos.NodeInfo toNodeInfo() {
        return MessageProtos.NodeInfo.newBuilder()
                .setNodeId(this.nodeId)
                .setPort(this.port)
                .setIpAddress(this.ipAddress.toString())
                .setType(MessageProtos.NodeInfo.InfoType.ALIVE)
                .build();
    }
}
