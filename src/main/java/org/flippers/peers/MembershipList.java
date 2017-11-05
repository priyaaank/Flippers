package org.flippers.peers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

public class MembershipList implements NodeStateObserver {

    private static final Integer START_INDEX = 0;
    private List<PeerNode> registeredMemberNodes;
    private List<PeerNode> ackAwaitedNodes;
    private List<PeerNode> indirectAckAwaitedNodes;
    private List<PeerNode> failureSuspectedNodes;

    public MembershipList() {
        this.registeredMemberNodes = synchronizedList(new ArrayList<PeerNode>());
        this.ackAwaitedNodes = synchronizedList(new ArrayList<PeerNode>());
        this.indirectAckAwaitedNodes = synchronizedList(new ArrayList<PeerNode>());
        this.failureSuspectedNodes = synchronizedList(new ArrayList<PeerNode>());
    }

    public List<PeerNode> selectNodesRandomly(Integer selectionCount) {
        if (registeredMemberNodes == null || registeredMemberNodes.size() == 0) return null;
        if (selectionCount >= registeredMemberNodes.size())
            throw new RuntimeException("Registered members are less than requested member count");
        return selectNodes(selectionCount);
    }

    public void add(PeerNode peerNode) {
        this.registeredMemberNodes.add(peerNode);
        peerNode.registerObserver(this);
        peerNode.initJoining();
    }

    public List<PeerNode> getNodesAwaitingAck() {
        return unmodifiableList(this.ackAwaitedNodes);
    }

    public Integer memberCount() {
        return this.registeredMemberNodes.size();
    }

    @Override
    public void markPingAwaited(PeerNode peerNode) {
        if (!this.ackAwaitedNodes.contains(peerNode))
            this.ackAwaitedNodes.add(peerNode);
    }

    @Override
    public void markIndirectPingAwaited(PeerNode peerNode) {
        if (!this.indirectAckAwaitedNodes.contains(peerNode))
            this.indirectAckAwaitedNodes.add(peerNode);
    }

    @Override
    public void markFailureSuspected(PeerNode peerNode) {
        if (!this.failureSuspectedNodes.contains(peerNode))
            this.failureSuspectedNodes.add(peerNode);
    }

    @Override
    public void markAlive(PeerNode peerNode) {
        if (!this.registeredMemberNodes.contains(peerNode))
            this.registeredMemberNodes.add(peerNode);

        this.ackAwaitedNodes.remove(peerNode);
        this.indirectAckAwaitedNodes.remove(peerNode);
        this.failureSuspectedNodes.remove(peerNode);
    }

    @Override
    public void markDead(PeerNode peerNode) {
        peerNode.deregisterObserver(this);

        this.registeredMemberNodes.remove(peerNode);
        this.ackAwaitedNodes.remove(peerNode);
        this.indirectAckAwaitedNodes.remove(peerNode);
        this.failureSuspectedNodes.remove(peerNode);
    }

    @Override
    public void markExited(PeerNode peerNode) {
        peerNode.deregisterObserver(this);

        this.registeredMemberNodes.remove(peerNode);
        this.ackAwaitedNodes.remove(peerNode);
        this.indirectAckAwaitedNodes.remove(peerNode);
        this.failureSuspectedNodes.remove(peerNode);
    }

    private List<PeerNode> selectNodes(Integer selectionCount) {
        return new Random()
                .ints(selectionCount, START_INDEX, registeredMemberNodes.size())
                .mapToObj(registeredMemberNodes::get)
                .collect(Collectors.toList());
    }

}
