package org.flippers.peers;

import org.flippers.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedList;
import static org.flippers.config.Config.DefaultValues.*;
import static org.flippers.config.Config.KeyNames.*;

public class MembershipList implements NodeStateObserver {

    private static final Integer START_INDEX = 0;
    private final List<PeerNode> registeredMemberNodes;
    private final List<PeerNode> ackAwaitedNodes;
    private final List<PeerNode> indirectAckAwaitedNodes;
    private final List<PeerNode> failureSuspectedNodes;
    private final Integer ackTimeoutMilliSeconds;
    private final Integer indirectAckTimeoutMilliSeconds;
    private final Integer failureSuspectHoldOffMilliSeconds;

    public MembershipList(Config config) {
        this.ackTimeoutMilliSeconds = config.getValue(ACK_TIMEOUT_MILLISECONDS, DEFAULT_ACK_TIMEOUT_MILLISECONDS);
        this.indirectAckTimeoutMilliSeconds = config.getValue(INDIRECT_ACK_TIMEOUT_MILLISECONDS, DEFAULT_INDIRECT_ACK_TIMEOUT_MILLISECONDS);
        this.failureSuspectHoldOffMilliSeconds = config.getValue(FAILURE_HOLD_OFF_MILLISECONDS, DEFAULT_FAILURE_HOLD_OFF_MILLISECONDS);
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
        return this.ackAwaitedNodes.stream()
                .filter(node -> !node.isInteractionInitiatedWithinMilliSeconds(ackTimeoutMilliSeconds))
                .collect(Collectors.toList());
    }

    public List<PeerNode> getNodesAwaitingIndirectAck() {
        return this.indirectAckAwaitedNodes.stream()
                .filter(node -> !node.isInteractionInitiatedWithinMilliSeconds(indirectAckTimeoutMilliSeconds))
                .collect(Collectors.toList());
    }

    public List<PeerNode> getNodesMarkedAsSuspect() {
        return this.failureSuspectedNodes.stream()
                .filter(node -> !node.isInteractionInitiatedWithinMilliSeconds(failureSuspectHoldOffMilliSeconds))
                .collect(Collectors.toList());
    }

    public Integer memberCount() {
        return this.registeredMemberNodes.size();
    }

    @Override
    public void markJoined(PeerNode peerNode) {
        //Do nothing
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
