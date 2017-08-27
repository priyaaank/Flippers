package org.flippers.agent.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionAcceptor {

    private static final Integer DEFAULT_PORT = 8343;
    private static final int DEFAULT_POOL_SIZE = 2;
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
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true) {
                ConnectedClient connectedClient = new ConnectedClient(serverSocket.accept());
                executor.submit(new ClientHandler(connectedClient));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ConnectionAcceptor().beginAccepting();
    }

}
