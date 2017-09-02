package org.flippers.agent.inbound;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageListener {

    private static final Integer DEFAULT_PORT = 8343;
    public static final int DEFAULT_ONE_KB_BUFFER = 1024;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private AtomicBoolean shutdownInitiated = new AtomicBoolean(Boolean.FALSE);
    private CountDownLatch countDownUntilBoundToPort = new CountDownLatch(1);
    private CountDownLatch countDownUntilShutdown = new CountDownLatch(1);
    private final Integer port;
    private MessageHandler messageHandler;
    private DatagramSocket socket;

    public MessageListener(Integer port, MessageHandler messageHandler) {
        this.port = port;
        this.messageHandler = messageHandler;
    }

    public MessageListener() {
        this(DEFAULT_PORT, new DefaultMessageHandler());
    }

    public void beginAccepting() {
        try {
            new Thread(this::bindAgentToSocket).start();
            countDownUntilBoundToPort.await();
        } catch (InterruptedException e) {
            shutdownInitiated.compareAndSet(Boolean.TRUE, Boolean.FALSE);
            LOGGER.error("Interrupted while starting agent");
            LOGGER.error(e.getMessage());
        }
    }

    private void bindAgentToSocket() {
        try {
            this.socket = new DatagramSocket(this.port);
            while (!shutdownInitiated.get()) {
                countDownUntilBoundToPort.countDown();
                DatagramPacket datagramPacket = new DatagramPacket(new byte[DEFAULT_ONE_KB_BUFFER], DEFAULT_ONE_KB_BUFFER);
                socket.receive(datagramPacket);
                DataMessage receivedMessage = new DataMessage(datagramPacket);
                messageHandler.handle(receivedMessage);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            countDownUntilShutdown.countDown();
            LOGGER.debug("Agent is shut down!");
        }
    }

    public void shutdownGracefully() throws InterruptedException {
        //eventually evolve to leave from cluster
        shutdownInitiated.compareAndSet(Boolean.FALSE, Boolean.TRUE);
        socket.close();
        countDownUntilShutdown.await();
    }

}
