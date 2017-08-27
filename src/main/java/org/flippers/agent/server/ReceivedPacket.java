package org.flippers.agent.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;

public class ReceivedPacket {

    static Logger LOGGER = LoggerFactory.getLogger(ReceivedPacket.class);

    private DatagramPacket packet;

    public ReceivedPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public void handle() {
        byte[] data = this.packet.getData();
        String parsedData = new String(data, 0, data.length);
        LOGGER.debug("Recieved data in a UDP packet : " + parsedData);
        System.out.println(parsedData);
    }
}
