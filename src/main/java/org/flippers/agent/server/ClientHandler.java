package org.flippers.agent.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler implements Runnable {

    static Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private ConnectedClient client;

    public ClientHandler(ConnectedClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        client.handle();
    }
}
