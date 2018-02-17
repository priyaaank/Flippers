package org.flippers.peers;

import org.flippers.peers.states.NodeState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PeerNodeTest {

    private PeerNode peerNode;

    @Mock
    InetAddress inetAddress;

    @Before
    public void setUp() throws Exception {
        this.peerNode = new PeerNode(inetAddress, 8180);
    }

    @Test
    public void shouldNotifyObserversOnJoining() throws Exception {
        MockNodeObserver observer = new MockNodeObserver();
        this.peerNode.registerObserver(observer);
        this.peerNode.initJoining();

        assertThat(observer.getState(), is("ALIVE"));
    }

    @Test
    public void shouldNotifyObserverWhenPingHasBeenInitiated() throws Exception {
        MockNodeObserver observer = new MockNodeObserver();
        this.peerNode.registerObserver(observer);
        this.peerNode.pingInitiated();

        assertThat(observer.getState(), is("PING_AWAITED"));
    }

    @Test
    public void shouldNotifyObserverWhenHasBeenMarkedAlive() throws Exception {
        MockNodeObserver observer = new MockNodeObserver();
        this.peerNode.registerObserver(observer);
        this.peerNode.markAlive();

        assertThat(observer.getState(), is("ALIVE"));
    }

    @Test
    public void shouldNotReceiveNotificationsWhenObserverIsNotRegistered() throws Exception {
        MockNodeObserver observer = new MockNodeObserver();
        this.peerNode.markAlive();

        assertThat(observer.getState(), is(nullValue()));
    }

    @Test
    public void shouldReturnPort() throws Exception {
        assertThat(peerNode.getPort(), is(8180));
    }

    @Test
    public void shouldReturnIpAddress() throws Exception {
        assertThat(peerNode.getIpAddress(), is(inetAddress));
    }

    @Test
    public void shouldMarkTwoNodesAsEqualsIfInetAddressAndPortsAreSame() throws Exception {
        PeerNode nodeToCompare = new PeerNode(inetAddress, 8180);
        assertEquals(peerNode, nodeToCompare);
    }

    class MockNodeObserver implements NodeStateObserver {

        private String state;

        public String getState() {
            return this.state;
        }

        @Override
        public void markJoined(PeerNode peerNode, NodeState fromState) {
            this.state = "JOINED";
        }

        @Override
        public void markPingAwaited(PeerNode peerNode, NodeState fromState) {
            this.state = "PING_AWAITED";
        }

        @Override
        public void markIndirectPingAwaited(PeerNode peerNode, NodeState fromState) {
            this.state = "INDIRECT_PING_AWAITED";
        }

        @Override
        public void markAlive(PeerNode peerNode, NodeState fromState) {
            this.state = "ALIVE";
        }

        @Override
        public void markDead(PeerNode peerNode, NodeState fromState) {
            this.state = "DEAD";
        }

        @Override
        public void markExited(PeerNode peerNode, NodeState fromState) {
            this.state = "EXITED";
        }

        @Override
        public void markFailureSuspected(PeerNode peerNode, NodeState fromState) {
            this.state = "FAILURE_SUSPECTED";
        }
    }

}