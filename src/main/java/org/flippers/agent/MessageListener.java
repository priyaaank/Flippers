package org.flippers.agent;

import org.flippers.handlers.HandlerExecutor;
import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.CountDownLatch;

public class MessageListener {

    private static final int DEFAULT_ONE_KB_BUFFER = 1024;
    private static final Integer DEFAULT_PORT = 8343;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private Boolean shutdownInitiated = Boolean.FALSE;
    private CountDownLatch countDownUntilBoundToPort = new CountDownLatch(1);
    private HandlerExecutor handlerExecutor;
    private DatagramSocket socket;

    public MessageListener(DatagramSocket socket, HandlerExecutor handlerExecutor) {
        this.handlerExecutor = handlerExecutor;
        this.socket = socket;
    }

    public void beginAccepting() {
        try {
            new Thread(this::bindAgentToSocket).start();
            countDownUntilBoundToPort.await();
        } catch (InterruptedException e) {
            shutdownInitiated = Boolean.TRUE;
            LOGGER.error("Interrupted while starting agent");
            LOGGER.error(e.getMessage());
        }
    }

    private void bindAgentToSocket() {
        try {
            while (!shutdownInitiated) {
                countDownUntilBoundToPort.countDown();
                DatagramPacket datagramPacket = new DatagramPacket(new byte[DEFAULT_ONE_KB_BUFFER], DEFAULT_ONE_KB_BUFFER);
                socket.receive(datagramPacket);
                DataMessage receivedMessage = new DataMessage(datagramPacket);
                handlerExecutor.executeHandler(receivedMessage);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            LOGGER.debug("Agent is shut down!");
        }
    }

}
