package org.flippers.peers.states;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AwaitingAckTest {

    @Mock
    private PeerNode peerNode;
    @Mock
    private NodeStateObserver observer;
    private ArrayList<NodeStateObserver> observers;
    private AwaitingAck awaitingAck;

    @Before
    public void setUp() {
        this.awaitingAck = new AwaitingAck();
        this.observers = new ArrayList<NodeStateObserver>() {{
            add(observer);
        }};
    }

    @Test
    public void shouldIndicatePingIsAwaited() {
        NodeState fromState = new Alive();
        this.awaitingAck.publishStateTransition(peerNode, observers, fromState);

        verify(observer).markPingAwaited(peerNode, fromState);
    }
}