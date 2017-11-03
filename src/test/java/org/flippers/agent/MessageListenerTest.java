package org.flippers.agent;

import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageProtos;
import org.flippers.messages.MessageType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static org.flippers.messages.MessageProtos.Message.MessageType.PING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageListenerTest {

    Integer portNumber = 8343;
    CountDownLatch awaitMessageHandling;
    DataMessage receivedMessage;
    MessageListener agent;
    MockPeerAgent peerAgent;
    DatagramSocket socket;

    @Before
    public void setUp() throws Exception {
        awaitMessageHandling = new CountDownLatch(1);
        this.socket = new DatagramSocket(this.portNumber);
        this.agent = new MessageListener(socket, packet -> {
            receivedMessage = packet;
            awaitMessageHandling.countDown();
        });
        this.peerAgent = new MockPeerAgent();
        this.agent.beginAccepting();
    }

    @After
    public void tearDown() throws Exception {
        if (!socket.isClosed()) this.socket.close();
    }

    @Test
    public void shouldAcceptAPacketAndPassToHandler() throws Exception {
        peerAgent.sendPingMessage();

        assertTrue(awaitMessageHandling.await(1, TimeUnit.SECONDS));
        assertEquals(MessageType.PING, receivedMessage.getMessageType());
    }

    @Test
    public void shouldNotAcceptMessagesPostShutdown() throws Exception {
        this.socket.close();
        peerAgent.sendPingMessage();

        assertFalse(awaitMessageHandling.await(1, TimeUnit.SECONDS));
    }

    class MockPeerAgent {

        void sendPingMessage() throws IOException {
            MessageProtos.Message message = MessageProtos.Message.newBuilder().setType(PING).build();
            byte[] payload = message.toByteArray();
            DatagramPacket packet = new DatagramPacket(payload, payload.length, InetAddress.getLocalHost(), portNumber);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
        }
    }
}