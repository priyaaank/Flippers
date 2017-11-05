package org.flippers.peers.states;

import org.flippers.peers.NodeStateObserver;
import org.flippers.peers.PeerNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DeadTest {

    @Mock
    private PeerNode peerNode;

    @Mock
    private NodeStateObserver observer;

    private ArrayList<NodeStateObserver> observers;
    private Dead dead;

    @Before
    public void setUp() throws Exception {
        this.dead = new Dead();
        this.observers = new ArrayList<NodeStateObserver>() {{
            add(observer);
        }};
    }

    @Test
    public void shouldIndicateNodeIsDead() throws Exception {
        this.dead.publishStateTransition(peerNode, observers);

        verify(observer).markDead(peerNode);
    }
}