package org.flippers.dissemination;

import org.flippers.peers.PeerNode;
import org.flippers.peers.states.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;

import static org.flippers.dissemination.EventType.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventGeneratorTest {

    private EventGenerator eventGenerator;
    private Integer defaultPort = 8383;
    private PeerNode peerNode;

    @Mock
    private EventLog eventLog;

    @Mock
    private InetAddress inetAddress;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;
    private String ipAddress;

    @Before
    public void setUp() throws Exception {
        this.eventGenerator = new EventGenerator(eventLog);
        this.peerNode = new PeerNode(inetAddress, defaultPort);
        this.ipAddress = "10.10.102.101";

        when(inetAddress.getHostAddress()).thenReturn(ipAddress);
    }

    @Test
    public void shouldGenerateJoinedEvent() {
        this.eventGenerator.markJoined(peerNode, new Dead());

        verify(eventLog).enqueue(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();

        assertThat(capturedEvent.getEventType(), is(JOINED));
        assertThat(capturedEvent.getHostIp(), is(ipAddress));
    }

    @Test
    public void shouldGenerateDeadEvent() {
        this.eventGenerator.markDead(peerNode, new FailureSuspected());

        verify(eventLog).enqueue(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();

        assertThat(capturedEvent.getEventType(), is(DEAD));
        assertThat(capturedEvent.getHostIp(), is(ipAddress));
    }

    @Test
    public void shouldGenerateExitedEvent() {
        this.eventGenerator.markExited(peerNode, new Alive());

        verify(eventLog).enqueue(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();

        assertThat(capturedEvent.getEventType(), is(EXITED));
        assertThat(capturedEvent.getHostIp(), is(ipAddress));
    }

    @Test
    public void shouldGenerateSuspectEvent() {
        this.eventGenerator.markFailureSuspected(peerNode, new AwaitingIndirectAck());

        verify(eventLog).enqueue(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();

        assertThat(capturedEvent.getEventType(), is(SUSPECT));
        assertThat(capturedEvent.getHostIp(), is(ipAddress));
    }

    @Test
    public void shouldGenerateAliveEvent() {
        this.eventGenerator.markAlive(peerNode, new FailureSuspected());

        verify(eventLog).enqueue(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();

        assertThat(capturedEvent.getEventType(), is(ALIVE));
        assertThat(capturedEvent.getHostIp(), is(ipAddress));
    }

    @Test
    public void shouldNotGenerateAnyEventForPingInitiation() {
        this.eventGenerator.markPingAwaited(peerNode, new Alive());

        verify(eventLog, never()).enqueue(any(Event.class));
    }

    @Test
    public void shouldNotGenerateAnyEventForIndirectPingInitiation() throws Exception {
        this.eventGenerator.markIndirectPingAwaited(peerNode, new AwaitingAck());

        verify(eventLog, never()).enqueue(any(Event.class));
    }
}