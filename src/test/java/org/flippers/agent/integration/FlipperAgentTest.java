package org.flippers.agent.integration;

import org.flippers.agent.inbound.DefaultMessageHandler;
import org.flippers.agent.inbound.MessageHandler;
import org.flippers.agent.inbound.MessageListener;
import org.flippers.agent.inbound.MessageType;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageProtos;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FlipperAgentTest {

    private MessageListener peerAgentUnderTest;
    private MessageListener testingAgent;
    private Integer peerPort;
    private Integer sourcePort;
    private CountDownLatch awaitUntilAckReceived = new CountDownLatch(1);
    private TestMessageHandler testMessageHandler;
    private InetAddress localIpAddress;

    @Before
    public void setUp() throws Exception {
        this.peerPort = 8323;
        this.sourcePort = 8324;
        this.peerAgentUnderTest = new MessageListener(peerPort, new DefaultMessageHandler());
        this.testMessageHandler = new TestMessageHandler(awaitUntilAckReceived);
        this.testingAgent = new MessageListener(this.sourcePort, testMessageHandler);
        this.localIpAddress = InetAddress.getLocalHost();
        this.peerAgentUnderTest.beginAccepting();
        this.testingAgent.beginAccepting();
    }

    @After
    public void tearDown() throws Exception {
        this.peerAgentUnderTest.shutdownGracefully();
        this.testingAgent.shutdownGracefully();
    }

    @Test
    public void shouldReceiveAnAckForAPing() throws Exception {
        sendPingMessage();
        assertTrue("Expected ACK for PING", awaitUntilAckReceived.await(60, TimeUnit.SECONDS));

        DataMessage receivedMessage = this.testMessageHandler.message;
        assertThat(receivedMessage.getMessageType(), is(MessageType.ACK));
        assertThat(receivedMessage.getSequenceNumber(), is(1L));
        assertThat(receivedMessage.getDestinationPort(), is(this.sourcePort));
        assertThat(receivedMessage.getInetAddress().getHostAddress(), is(localIpAddress.getHostAddress()));
        assertThat(receivedMessage.getSourcePort(), is(this.peerPort));
    }

    private void sendPingMessage() throws IOException {
        DataMessage message = new DataMessage(1L, localIpAddress, MessageType.PING, this.peerPort, this.sourcePort);
        new DatagramSocket().send(message.toDatagramPacket());
    }

    private class TestMessageHandler implements MessageHandler {

        private CountDownLatch countDownLatch;
        public DataMessage message;

        public TestMessageHandler(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void handle(DataMessage packet) {
            this.message = packet;
            this.countDownLatch.countDown();
        }
    }

}
