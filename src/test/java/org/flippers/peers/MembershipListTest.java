package org.flippers.peers;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class MembershipListTest {

    private MembershipList membershipList;

    @Before
    public void setUp() throws Exception {
        this.membershipList = new MembershipList();
    }

    private void registerNMemberNodes() throws UnknownHostException {
        this.membershipList = new MembershipList();
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
    public void shouldMarkANodePingInitiated() throws Exception {
        registerNMemberNodes();
        PeerNode peerNode = this.membershipList.selectNodesRandomly(1).get(0);
        peerNode.pingInitiated();
        List<PeerNode> nodesAwaitingAck = this.membershipList.getNodesAwaitingAck();

        assertThat(nodesAwaitingAck.size(), is(1));
        assertThat(nodesAwaitingAck.get(0), is(peerNode));
    }
}
