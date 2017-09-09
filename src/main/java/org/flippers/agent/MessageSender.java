package org.flippers.agent;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;

public class MessageSender {

    private static Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);
    private DatagramSocket socket;
    private ExecutorService poolExecutor;

    public MessageSender(DatagramSocket socket, ExecutorService poolExecutor) {
        this.socket = socket;
        this.poolExecutor = poolExecutor;
    }

    public void send(DataMessage message) {
        this.poolExecutor.submit(() -> {
            try {
                if(socket.isClosed()) throw new RuntimeException("Socket has been closed.");
                socket.send(message.toDatagramPacket());
            } catch (IOException e) {
                LOGGER.error(String.format("Failed sending of message: %s", new String(message.toDatagramPacket().getData())));
                LOGGER.error(e.getMessage());
            }
        });
    }
}
