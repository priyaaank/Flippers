package org.flippers.agent.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectedClient {

    static Logger LOGGER = LoggerFactory.getLogger(ConnectedClient.class);

    private Socket clientSocket;

    public ConnectedClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handle() {
        try (BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line;
            while ((line = bufferedInputStream.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage());
        }
    }
}
