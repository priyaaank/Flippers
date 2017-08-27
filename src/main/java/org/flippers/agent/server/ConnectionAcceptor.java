package org.flippers.agent.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionAcceptor {

    private static final Integer DEFAULT_PORT = 8343;
    private static final int DEFAULT_POOL_SIZE = 2;
    public static final int DEFAULT_MSG_BUFFER_SIZE = 256;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionAcceptor.class);

    private final Integer port;
    private final ExecutorService executor;

    private ConnectionAcceptor(Integer port, ExecutorService executorService) {
        this.port = port;
        this.executor = executorService;
    }

    private ConnectionAcceptor() {
        this(DEFAULT_PORT, Executors.newFixedThreadPool(DEFAULT_POOL_SIZE));
    }

    public void beginAccepting() {
        try(DatagramSocket serverSocket = new DatagramSocket(this.port)) {
            while(true) {
                byte[] buf = new byte[DEFAULT_MSG_BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                serverSocket.receive(packet);
                ReceivedPacket receivedPacket = new ReceivedPacket(packet);
                executor.submit(new PacketHandler(receivedPacket));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ConnectionAcceptor().beginAccepting();
    }

}
