package org.flippers.messages;

import org.flippers.config.FileConfig;
import org.flippers.peers.PeerNode;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.flippers.messages.MessageType.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageCreatorTest {

    private MessageCreator messageCreator;
    private PeerNode peerNodeAlpha;
    private FileConfig config;
    private PeerNode peerNodeBeta;

    @Before
    public void setUp() throws Exception {
        this.config = new FileConfig();
        this.peerNodeBeta = PeerNode.nodeFor(InetAddress.getLocalHost(), 8000);
        this.peerNodeAlpha = new PeerNode(InetAddress.getLocalHost(), 8083);
        this.messageCreator = new MessageCreator(config, peerNodeAlpha);
    }

    @Test
    public void shouldCraftAMessageForPing() {
        DataMessage pingMessage = messageCreator.craftPingMsg(peerNodeBeta);

        assertThat(pingMessage.getMessageType(), is(PING));
        assertThat(pingMessage.getSourceNode(), is(peerNodeAlpha));
        assertThat(pingMessage.getDestinationNode(), is(this.peerNodeBeta));
    }

    @Test
    public void shouldCraftAMessageForIndirectPing() {
        DataMessage indirectPingMsg = messageCreator.craftIndirectPingMsg(peerNodeBeta);

        assertThat(indirectPingMsg.getMessageType(), is(PING_REQ));
        assertThat(indirectPingMsg.getSourceNode(), is(peerNodeAlpha));
        assertThat(indirectPingMsg.getDestinationNode(), is(peerNodeBeta));
    }

    @Test
    public void shouldCraftAMessageForAnAckResponse() {
        DataMessage pingMessage = messageCreator.craftPingMsg(peerNodeBeta);
        DataMessage ackResponse = new MessageCreator(config, peerNodeBeta).ackResponseForPingMsg(pingMessage);

        assertThat(ackResponse.getMessageType(), is(ACK));
        assertThat(ackResponse.getDestinationNode(), is(peerNodeAlpha));
        assertThat(ackResponse.getSourceNode(), is(peerNodeBeta));
    }

}