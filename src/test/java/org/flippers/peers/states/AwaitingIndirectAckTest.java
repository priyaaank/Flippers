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
public class AwaitingIndirectAckTest {

    @Mock
    private PeerNode peerNode;

    @Mock
    private NodeStateObserver observer;

    private ArrayList<NodeStateObserver> observers;
    private AwaitingIndirectAck awaitingIndirectAck;

    @Before
    public void setUp() {
        this.awaitingIndirectAck = new AwaitingIndirectAck();
        this.observers = new ArrayList<NodeStateObserver>() {{
            add(observer);
        }};
    }

    @Test
    public void shouldIndicateIndirectPingIsAwaited() {
        AwaitingAck fromState = new AwaitingAck();
        this.awaitingIndirectAck.publishStateTransition(peerNode, observers, fromState);

        verify(observer).markIndirectPingAwaited(peerNode, fromState);
    }
}