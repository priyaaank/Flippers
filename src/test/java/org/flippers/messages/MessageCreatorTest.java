package org.flippers.messages;

import org.flippers.config.FileConfig;
import org.flippers.peers.PeerNode;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.flippers.messages.MessageType.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageCreatorTest {

    private MessageCreator messageCreator;
    private PeerNode peerNode;
    private FileConfig config;

    @Before
    public void setUp() throws Exception {
        this.config = new FileConfig();
        this.messageCreator = new MessageCreator(config);
        peerNode = new PeerNode(InetAddress.getLocalHost(), 8083);
    }

    @Test
    public void shouldCraftAMessageForPing() throws UnknownHostException {
        DataMessage pingMessage = messageCreator.craftPingMsg(peerNode);

        assertThat(pingMessage.getMessageType(), is(PING));
        assertThat(pingMessage.getSourceAddress(), is(InetAddress.getLocalHost()));
        assertThat(pingMessage.getDestinationPort(), is(8083));
        assertThat(pingMessage.getSourcePort(), is(8000));
    }

    @Test
    public void shouldCraftAMessageForIndirectPing() throws UnknownHostException {
        DataMessage indirectPingMsg = messageCreator.craftIndirectPingMsg(peerNode);

        assertThat(indirectPingMsg.getMessageType(), is(PING_REQ));
        assertThat(indirectPingMsg.getSourceAddress(), is(InetAddress.getLocalHost()));
        assertThat(indirectPingMsg.getDestinationPort(), is(8083));
        assertThat(indirectPingMsg.getSourcePort(), is(8000));
    }

    @Test
    public void shouldCraftAMessageForAnAckResponse() throws UnknownHostException {
        DataMessage pingMessage = messageCreator.craftPingMsg(peerNode);
        DataMessage ackResponse = messageCreator.ackResponseForPingMsg(pingMessage);

        assertThat(ackResponse.getMessageType(), is(ACK));
        assertThat(ackResponse.getSourceAddress(), is(InetAddress.getLocalHost()));
        assertThat(ackResponse.getDestinationPort(), is(8000));
        assertThat(ackResponse.getSourcePort(), is(8000));
    }

}