package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.config.Config;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;

import static org.flippers.messages.MessageType.ACK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PingHandlerTest {

    public static final int SOURCE_PORT = 9023;
    public static final int DESTINATION_PORT = 8182;
    private PingHandler pingHandler;

    @Mock
    MessageSender sender;

    @Mock
    Config config;

    @Mock
    InetAddress mockAddress;

    @Captor
    ArgumentCaptor<DataMessage> messageCaptor;

    @Before
    public void setUp() throws Exception {
        this.pingHandler = new PingHandler(sender, config);
        when(config.getListenPort()).thenReturn(DESTINATION_PORT);
    }

    @Test
    public void shouldResponseWithAnAckMessage() throws Exception {
        long sequenceNumber = 123L;
        DataMessage message = new DataMessage(sequenceNumber, mockAddress, MessageType.PING, DESTINATION_PORT, SOURCE_PORT);
        this.pingHandler.handle(message);

        verify(sender).send(messageCaptor.capture());
        DataMessage interceptedMessage = messageCaptor.getValue();

        assertThat(interceptedMessage.getMessageType(), is(ACK));
        assertThat(interceptedMessage.getInetAddress(), is(mockAddress));
        assertThat(interceptedMessage.getSequenceNumber(), is(sequenceNumber));
        assertThat(interceptedMessage.getSourcePort(), is(DESTINATION_PORT));
        assertThat(interceptedMessage.getDestinationPort(), is(SOURCE_PORT));
    }
}