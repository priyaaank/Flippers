package org.flippers.agent;

import org.flippers.config.FileConfig;
import org.flippers.handlers.HandlerExecutor;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageType;
import org.flippers.peers.PeerNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FlipperAgentTest {

    private Integer portForAgentUnderTest = 8000;
    private Integer portForTestingAgent = 9000;
    private FileConfig configForAgentUnderTest = new FileConfig();
    private FlipperAgent peerAgentUnderTest;
    private InetAddress localIpAddress;
    private DatagramSocket socket;
    private TestReceivedMessageHandler receivedReceivedMessageHandler;
    private CountDownLatch awaitUntilAckReceived = new CountDownLatch(1);
    private MessageListener testingAgentListener;
    private MessageSender testingAgentSender;
    private PeerNode testingNode;
    private PeerNode nodeBeingTested;

    @Before
    public void setUp() throws Exception {
        this.peerAgentUnderTest = new FlipperAgent(configForAgentUnderTest);
        this.peerAgentUnderTest.start();
        this.localIpAddress = InetAddress.getLocalHost();
        this.socket = new DatagramSocket(this.portForTestingAgent);
        this.receivedReceivedMessageHandler = new TestReceivedMessageHandler(awaitUntilAckReceived);
        this.testingAgentListener = new MessageListener(this.socket, this.receivedReceivedMessageHandler);
        this.testingAgentListener.beginAccepting();
        this.testingAgentSender = new MessageSender(this.socket, Executors.newSingleThreadExecutor());
        this.testingNode = PeerNode.nodeFor(localIpAddress, this.portForTestingAgent);
        this.nodeBeingTested = PeerNode.nodeFor(localIpAddress, this.portForAgentUnderTest);
    }

    @After
    public void tearDown() {
        peerAgentUnderTest.stop();
        this.socket.close();
    }

    @Test
    public void shouldReceiveAnAckForAPing() throws Exception {
        sendPingMessage();
        assertTrue("Expected ACK for PING", awaitUntilAckReceived.await(10, TimeUnit.SECONDS));

        DataMessage receivedMessage = this.receivedReceivedMessageHandler.message;
        assertThat(receivedMessage.getMessageType(), is(MessageType.ACK));
        assertThat(receivedMessage.getSequenceNumber(), is("123"));
        assertThat(receivedMessage.getSourceNode(), is(nodeBeingTested));
    }

    private void sendPingMessage() {
        DataMessage message = new DataMessage("123", testingNode, MessageType.PING, nodeBeingTested);
        this.testingAgentSender.send(message);
    }

    private class TestReceivedMessageHandler implements HandlerExecutor {

        private CountDownLatch countDownLatch;
        public DataMessage message;

        public TestReceivedMessageHandler(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void executeHandler(DataMessage packet) {
            this.message = packet;
            this.countDownLatch.countDown();
        }
    }

}
