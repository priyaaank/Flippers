package org.flippers.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionAcceptor {

    private static ConnectionAcceptor singleInstance;
    private final Integer port;

    static final Logger LOGGER = LoggerFactory.getLogger(ConnectionAcceptor.class);

    private ConnectionAcceptor() {
        this.port = 00000;
    }

    public static ConnectionAcceptor instance() {
        if (singleInstance == null) {
            singleInstance = new ConnectionAcceptor();
        }
        return singleInstance;
    }

    public void beginAccepting() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
