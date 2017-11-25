package org.flippers.agent;

import org.flippers.messages.MessageCreator;
import org.flippers.peers.MembershipList;
import org.flippers.peers.PeerNode;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FailureDetector implements Runnable {

    private ScheduledExecutorService executorService;
    private MembershipList membershipList;
    private MessageSender sender;
    private MessageCreator messageCreator;

    public FailureDetector(ScheduledExecutorService executorService, MembershipList membershipList, MessageSender sender, MessageCreator messageCreator) {
        this.executorService = executorService;
        this.membershipList = membershipList;
        this.sender = sender;
        this.messageCreator = messageCreator;
    }

    public void startDetection() {
        executorService.scheduleAtFixedRate(FailureDetector.this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        initiatePing();
        initiateIndirectPing();
        markNodesSuspected();
        markNodesFailed();
    }

    private void initiateIndirectPing() {
        List<PeerNode> peerNodes = this.membershipList.getNodesAwaitingAck();
        peerNodes.forEach(this::sendIndirectPing);
    }

    private void initiatePing() {
        List<PeerNode> peerNodes = this.membershipList.selectNodesRandomly(5);
        peerNodes.forEach(this::sendPing);
    }

    private void markNodesSuspected() {
        List<PeerNode> peerNodes = this.membershipList.getNodesAwaitingIndirectAck();
        peerNodes.forEach(PeerNode::markSuspect);
    }

    private void markNodesFailed() {
        List<PeerNode> peerNodes = this.membershipList.getNodesMarkedAsSuspect();
        peerNodes.forEach(PeerNode::markDead);
    }

    private void sendIndirectPing(PeerNode peerNode) {
        this.sender.send(messageCreator.craftIndirectPingMsg(peerNode));
        peerNode.indirectPingInitiated();
    }

    private void sendPing(final PeerNode peerNode) {
        this.sender.send(messageCreator.craftPingMsg(peerNode));
        peerNode.pingInitiated();
    }

}

