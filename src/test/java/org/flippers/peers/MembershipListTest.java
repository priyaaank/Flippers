package org.flippers.peers;

import org.flippers.config.Config;
import org.flippers.config.FileConfig;
import org.flippers.dissemination.EventGenerator;
import org.flippers.dissemination.EventLog;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class MembershipListTest {

    public static final int LESS_THAN_WAIT_THRESHOLD_MILLIS = 1;
    public static final int PAST_WAIT_THRESHOLD_MILLIS = 80;
    public static final int NO_RECORDS = 0;
    private MembershipList membershipList;

    private Config config;
    private EventGenerator eventGenerator;

    @Before
    public void setUp() throws Exception {
        this.config = new FileConfig();
        this.eventGenerator = new EventGenerator(new EventLog(config));
        this.membershipList = new MembershipList(config, eventGenerator);
    }

    private void registerNMemberNodes() throws UnknownHostException {
        this.membershipList = new MembershipList(config, eventGenerator);
        this.membershipList.add(buildTestNode(0));
        this.membershipList.add(buildTestNode(1));
        this.membershipList.add(buildTestNode(2));
        this.membershipList.add(buildTestNode(3));
        this.membershipList.add(buildTestNode(4));
    }

    private PeerNode buildTestNode(int i) throws UnknownHostException {
        return new PeerNode(InetAddress.getByName("127.0.0.1"), Integer.valueOf(834 + "" + i));
    }

    @Test
    public void shouldReturnTheCountOfMembers() throws Exception {
        registerNMemberNodes();

        assertThat(membershipList.memberCount(), is(5));
    }

    @Test
    public void shouldReturnNullWhenNoNodesAreRegistered() throws Exception {
        List<PeerNode> peerNodes = this.membershipList.selectNodesRandomly(1);

        assertThat(peerNodes, is(nullValue()));
    }

    @Test
    public void shouldBeAbleToRandomlySelectAndReturnNNodes() throws Exception {
        registerNMemberNodes();
        List<PeerNode> selectNodes = this.membershipList.selectNodesRandomly(3);

        assertThat(selectNodes.size(), is(3));
    }

    @Test
    public void shouldNotReturnNodeAwaitingForAckIfWaitThresholdIsNotPast() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.pingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingAck = this.membershipList.getNodesAwaitingAck();
        assertThat(nodesAwaitingAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldReturnNodeAwaitingForAckIfWaitThresholdIsPast() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.pingInitiated();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingAck = this.membershipList.getNodesAwaitingAck();
        assertThat(nodesAwaitingAck.size(), is(1));
        assertThat(nodesAwaitingAck.get(0), is(peerNode));
    }

    @Test
    public void shouldNotReturnNodeAwaitingForIndirectAckIfWaitThresholdIsNotPast() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.indirectPingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingIndirectAck = this.membershipList.getNodesAwaitingIndirectAck();
        assertThat(nodesAwaitingIndirectAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldReturnNodeAwaitingIndirectAckIfWaitThresholdIsPast() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.indirectPingInitiated();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingIndirectAck = this.membershipList.getNodesAwaitingIndirectAck();
        assertThat(nodesAwaitingIndirectAck.size(), is(1));
        assertThat(nodesAwaitingIndirectAck.get(0), is(peerNode));
    }


    @Test
    public void shouldNotReturnNodeSuspectedOfFailureIfWaitThresholdIsNotPast() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.markSuspect();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesMarkedAsSuspects = this.membershipList.getNodesMarkedAsSuspect();
        assertThat(nodesMarkedAsSuspects.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldReturnNodeSuspectedOfFailureIfWaitThresholdIsPast() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.markSuspect();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodeMarkedAsSuspects = this.membershipList.getNodesMarkedAsSuspect();
        assertThat(nodeMarkedAsSuspects.size(), is(1));
        assertThat(nodeMarkedAsSuspects.get(0), is(peerNode));
    }

    @Test(expected = RuntimeException.class)
    public void shouldRemoveNodeFromListOfAliveNodesOnceItIsMarkedDead() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.markDead();

        this.membershipList.selectNodesRandomly(5);

        fail("Expected runtime exception to be thrown");
    }

    @Test(expected = RuntimeException.class)
    public void shouldRemoveNodeFromListOfAliveNodesOnceItIsMarkedExited() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.markExited();

        this.membershipList.selectNodesRandomly(5);

        fail("Expected runtime exception to be thrown");
    }

    @Test
    public void shouldMoveNodeOutOfSuspectListIfMarkedAlive() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.markSuspect();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markAlive();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesMarkedAsSuspect = this.membershipList.getNodesMarkedAsSuspect();
        assertThat(nodesMarkedAsSuspect.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveNodeOutOfAwaitingAckStateOnceMarkedAlive() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.pingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markAlive();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingAck = this.membershipList.getNodesAwaitingAck();
        assertThat(nodesAwaitingAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveOutNodeFromAwaitingIndirectAckOnceMarkedAlive() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.indirectPingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markAlive();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingIndirectAck = this.membershipList.getNodesAwaitingIndirectAck();
        assertThat(nodesAwaitingIndirectAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveANodeOutOfPingAwaitedWhenMarkedExited() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.pingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markExited();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingAck = this.membershipList.getNodesAwaitingAck();
        assertThat(nodesAwaitingAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveANodeOutOfIndirectPingAwaitedWhenMarkedExited() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.indirectPingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markExited();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingIndirectAck = this.membershipList.getNodesAwaitingIndirectAck();
        assertThat(nodesAwaitingIndirectAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveANodeOutOfFailureSuspectListWhenMarkedExited() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.markSuspect();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markExited();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesMarkedAsSuspects = this.membershipList.getNodesMarkedAsSuspect();
        assertThat(nodesMarkedAsSuspects.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveANodeOutOfPingAwaitedWhenMarkedDead() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.pingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markDead();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingAck = this.membershipList.getNodesAwaitingAck();
        assertThat(nodesAwaitingAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveANodeOutOfIndirectPingAwaitedWhenMarkedDead() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.indirectPingInitiated();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markDead();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesAwaitingIndirectAck = this.membershipList.getNodesAwaitingIndirectAck();
        assertThat(nodesAwaitingIndirectAck.size(), is(NO_RECORDS));
    }

    @Test
    public void shouldMoveANodeOutOfFailureSuspectListWhenMarkedDead() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.markSuspect();

        sleep(LESS_THAN_WAIT_THRESHOLD_MILLIS);
        peerNode.markDead();

        sleep(PAST_WAIT_THRESHOLD_MILLIS);

        List<PeerNode> nodesMarkedAsSuspects = this.membershipList.getNodesMarkedAsSuspect();
        assertThat(nodesMarkedAsSuspects.size(), is(NO_RECORDS));
    }
}
