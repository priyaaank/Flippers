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
public class JoinedTest {

    @Mock
    private PeerNode peerNode;

    @Mock
    private NodeStateObserver observer;

    private ArrayList<NodeStateObserver> observers;
    private Joined joined;

    @Before
    public void setUp() throws Exception {
        this.joined = new Joined();
        this.observers = new ArrayList<NodeStateObserver>() {{
            add(observer);
        }};
    }

    @Test
    public void shouldIndicateThatNodeHasJoined() throws Exception {
        this.joined.publishStateTransition(peerNode, observers);

        verify(peerNode).markAlive();
    }
}