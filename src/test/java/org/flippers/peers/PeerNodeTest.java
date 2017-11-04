package org.flippers.peers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PeerNodeTest {

    private PeerNode peerNode;

    @Mock
    SubscriberAction subscriberAction;

    @Before
    public void setUp() throws Exception {
        this.peerNode = new PeerNode(InetAddress.getLocalHost(), 8180);
    }

    @Test
    public void shouldPublishStateChangeToObserverOnceRegistered() throws Exception {
        peerNode.registerObserver(updatedNode -> subscriberAction.execute());
        peerNode.pingInitiated();

        verify(subscriberAction, times(1)).execute();
    }

    @Test
    public void shouldNotPublishStateChangeToObserverOnceDeregistered() throws Exception {
        NodeStateObserver nodeStateObserver = updatedNode -> subscriberAction.execute();
        peerNode.registerObserver(nodeStateObserver);
        peerNode.deregisterObserver(nodeStateObserver);
        peerNode.pingInitiated();

        verify(subscriberAction, never()).execute();
    }

    interface SubscriberAction {
        void execute();
    }

}