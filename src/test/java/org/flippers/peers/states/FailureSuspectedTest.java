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
public class FailureSuspectedTest {

    @Mock
    private PeerNode peerNode;

    @Mock
    private NodeStateObserver observer;

    private ArrayList<NodeStateObserver> observers;
    private FailureSuspected failureSuspected;

    @Before
    public void setUp() {
        this.failureSuspected = new FailureSuspected();
        this.observers = new ArrayList<NodeStateObserver>() {{
            add(observer);
        }};
    }

    @Test
    public void shouldIndicateThatFailureIsSuspected() {
        NodeState fromState = new AwaitingIndirectAck();
        this.failureSuspected.publishStateTransition(peerNode, observers, fromState);

        verify(observer).markFailureSuspected(peerNode, fromState);
    }

}