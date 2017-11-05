package org.flippers.peers.states;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeStateFactoryTest {

    private NodeStateFactory nodeStateFactory;

    @Before
    public void setUp() throws Exception {
        this.nodeStateFactory = NodeStateFactory.getInstance();
    }

    @Test
    public void shouldReturnAliveState() throws Exception {
        assertTrue(this.nodeStateFactory.aliveState() instanceof Alive);
        assertSame(this.nodeStateFactory.aliveState(), this.nodeStateFactory.aliveState());
    }

    @Test
    public void shouldReturnJoinedState() throws Exception {
        assertTrue(this.nodeStateFactory.joinedState() instanceof Joined);
        assertSame(this.nodeStateFactory.joinedState(), this.nodeStateFactory.joinedState());
    }

    @Test
    public void shouldReturnAwaitingAckState() throws Exception {
        assertTrue(this.nodeStateFactory.awaitingAckState() instanceof AwaitingAck);
        assertSame(this.nodeStateFactory.awaitingAckState(), this.nodeStateFactory.awaitingAckState());
    }

    @Test
    public void shouldReturnAwatingIndirectAckState() throws Exception {
        assertTrue(this.nodeStateFactory.awaitingIndirectAckState() instanceof AwaitingIndirectAck);
        assertSame(this.nodeStateFactory.awaitingIndirectAckState(), this.nodeStateFactory.awaitingIndirectAckState());
    }

    @Test
    public void shouldReturnDeadState() throws Exception {
        assertTrue(this.nodeStateFactory.deadState() instanceof Dead);
        assertSame(this.nodeStateFactory.deadState(), this.nodeStateFactory.deadState());
    }

    @Test
    public void shouldReturnAwatingExitedState() throws Exception {
        assertTrue(this.nodeStateFactory.exitedState() instanceof Exited);
        assertSame(this.nodeStateFactory.exitedState(), this.nodeStateFactory.exitedState());
    }

    @Test
    public void shouldReturnFailureSuspectedState() throws Exception {
        assertTrue(this.nodeStateFactory.failureSuspectedState() instanceof FailureSuspected);
        assertSame(this.nodeStateFactory.failureSuspectedState(), this.nodeStateFactory.failureSuspectedState());
    }
}