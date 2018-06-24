package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.config.FileConfig;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageCreator;
import org.flippers.messages.MessageType;
import org.flippers.peers.PeerNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;

import static org.flippers.config.Config.DefaultValues.DEFAULT_LISTEN_PORT;
import static org.flippers.config.Config.KeyNames.LISTEN_PORT;
import static org.flippers.messages.MessageType.ACK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
    FileConfig config;

    @Mock
    InetAddress mockAddress;

    @Mock
    MessageCreator messageCreator;

    @Captor
    ArgumentCaptor<DataMessage> messageCaptor;

    @Mock
    DataMessage mockDataMessage;

    PeerNode currentNode;

    PeerNode peerNode;

    @Before
    public void setUp() {
        this.pingHandler = new PingHandler(sender, messageCreator);
        when(config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT)).thenReturn(DESTINATION_PORT);
        when(messageCreator.ackResponseForPingMsg(any(DataMessage.class))).thenReturn(mockDataMessage);
    }

    @Test
    public void shouldResponseWithAnAckMessage() throws Exception {
        String sequenceNumber = "1234";
        DataMessage message = new DataMessage(sequenceNumber, peerNode, MessageType.PING, currentNode);
        this.pingHandler.handle(message);

        verify(sender).send(messageCaptor.capture());
        DataMessage interceptedMessage = messageCaptor.getValue();

        assertThat(interceptedMessage, is(mockDataMessage));
    }
}