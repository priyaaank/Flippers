package org.flippers.agent.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketHandler implements Runnable {

    static Logger LOGGER = LoggerFactory.getLogger(PacketHandler.class);
    private ReceivedPacket client;

    public PacketHandler(ReceivedPacket client) {
        this.client = client;
    }

    @Override
    public void run() {
        client.handle();
    }
}
