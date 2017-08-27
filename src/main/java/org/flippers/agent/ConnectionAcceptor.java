package org.flippers.agent;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionAcceptor {

    private static ConnectionAcceptor singleInstance;
    private final Integer port;

    private ConnectionAcceptor() {
        this.port = 8752;
    }

    public static ConnectionAcceptor instance() {
        if(singleInstance == null) {
            singleInstance = new ConnectionAcceptor();
        }
        return singleInstance;
    }

    public void beginAccepting() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
