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
            //fix this. We should countdown with a timeout
            countDownUntilBoundToPort.await();
            LOGGER.debug("Listener binding complete. Listening for peers on port {}", socket.getPort());
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
                DataMessage receivedMessage = DataMessage.fromReceivedDatagram(datagramPacket);
                LOGGER.debug("Received message {}", receivedMessage);
                handlerExecutor.executeHandler(receivedMessage);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            socket.close();
            LOGGER.debug("Agent is shut down!");
        }
    }

}
