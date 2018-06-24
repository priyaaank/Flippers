package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.config.Config;
import org.flippers.config.FileConfig;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageCreator;
import org.flippers.messages.MessageType;
import org.flippers.peers.MembershipList;
import org.flippers.peers.PeerNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.flippers.config.Config.DefaultValues.DEFAULT_LISTEN_PORT;
import static org.flippers.messages.MessageType.ACK_JOIN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoinHandlerTest {

    private JoinHandler joinHandler;
    private Config fileConfig;

    @Mock
    private MessageSender sender;

    @Mock
    private MembershipList membershipList;

    @Mock
    private PeerNode peerNode;

    @Captor
    private ArgumentCaptor<DataMessage> messageCaptor;

    @Mock
    private MessageCreator messageCreator;

    @Mock
    private DataMessage mockAckMessage;

    @Before
    public void setUp() {
        this.fileConfig = new FileConfig();
        this.joinHandler = new JoinHandler(sender, membershipList, messageCreator);
        when(membershipList.forNode(any(PeerNode.class))).thenReturn(peerNode);
        when(messageCreator.ackResponseForJoinMsg(any(DataMessage.class))).thenReturn(mockAckMessage);
    }

    @Test
    public void shouldBeAnInstanceOfHandler() {
        assertTrue(joinHandler instanceof Handler);
    }

    @Test
    public void shouldRespondWithAckToJoinRequest() throws UnknownHostException {
        String sequenceNumber = "123";
        DataMessage joinMessage = getJoinMessage(sequenceNumber);
        joinHandler.handle(joinMessage);

        verify(sender).send(messageCaptor.capture());
        DataMessage ackMessage = messageCaptor.getValue();

        assertThat(ackMessage, is(mockAckMessage));
    }

    @Test
    public void shouldAddItToMembershipList() throws UnknownHostException {
        String sequenceNumber = "123";
        DataMessage joinMessage = getJoinMessage(sequenceNumber);
        joinHandler.handle(joinMessage);

        verify(membershipList).forNode(joinMessage.getSourceNode());
        verify(peerNode).initJoining();
    }

    private DataMessage getJoinMessage(String sequenceNumber) throws UnknownHostException {
        return new DataMessage(sequenceNumber, PeerNode.nodeFor(InetAddress.getLocalHost(), 8000),
                MessageType.JOIN, PeerNode.nodeFor(InetAddress.getLocalHost(), 8383));
    }
}