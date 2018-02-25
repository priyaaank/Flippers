package org.flippers.agent;

import org.flippers.config.Config;
import org.flippers.messages.MessageCreator;
import org.flippers.peers.MembershipList;
import org.flippers.peers.PeerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.flippers.config.Config.DefaultValues.DEFAULT_FAILURE_DETECTION_DELAY_PERIOD;
import static org.flippers.config.Config.DefaultValues.DEFAULT_FAILURE_DETECTION_INITIAL_DELAY;
import static org.flippers.config.Config.DefaultValues.DEFAULT_RANDOM_NODE_SELECTION_COUNT;
import static org.flippers.config.Config.KeyNames.FAILURE_DETECTION_DELAY_PERIOD;
import static org.flippers.config.Config.KeyNames.FAILURE_DETECTION_INITIAL_DELAY;
import static org.flippers.config.Config.KeyNames.RANDOM_NODE_SELECTION_COUNT;

public class FailureDetector implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(FailureDetector.class);
    private ScheduledExecutorService executorService;
    private MembershipList membershipList;
    private MessageSender sender;
    private MessageCreator messageCreator;
    private Config config;

    public FailureDetector(ScheduledExecutorService executorService, MembershipList membershipList, MessageSender sender, MessageCreator messageCreator, Config config) {
        this.executorService = executorService;
        this.membershipList = membershipList;
        this.sender = sender;
        this.messageCreator = messageCreator;
        this.config = config;
    }

    public void startDetection() {
        int initialDelay = config.getValue(FAILURE_DETECTION_INITIAL_DELAY, DEFAULT_FAILURE_DETECTION_INITIAL_DELAY);
        int delayPeriod = config.getValue(FAILURE_DETECTION_DELAY_PERIOD, DEFAULT_FAILURE_DETECTION_DELAY_PERIOD);
        executorService.scheduleAtFixedRate(this, initialDelay, delayPeriod, TimeUnit.MILLISECONDS);
        LOGGER.debug("Started failure detection");
    }

    @Override
    public void run() {
        initiatePing();
        initiateIndirectPing();
        markNodesSuspected();
        markNodesFailed();
    }

    private void initiateIndirectPing() {
        LOGGER.debug("Initiating indirect pings");
        List<PeerNode> peerNodes = this.membershipList.getNodesAwaitingAck();
        peerNodes.forEach(this::sendIndirectPing);
    }

    private void initiatePing() {
        LOGGER.debug("Initiating direct pings");
        Integer selectionCount = config.getValue(RANDOM_NODE_SELECTION_COUNT, DEFAULT_RANDOM_NODE_SELECTION_COUNT);
        List<PeerNode> peerNodes = this.membershipList.selectNodesRandomly(selectionCount);
        peerNodes.forEach(this::sendPing);
    }

    private void markNodesSuspected() {
        LOGGER.debug("Marking nodes suspected");
        List<PeerNode> peerNodes = this.membershipList.getNodesAwaitingIndirectAck();
        peerNodes.forEach(PeerNode::markSuspect);
    }

    private void markNodesFailed() {
        LOGGER.debug("Marking nodes failed");
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

