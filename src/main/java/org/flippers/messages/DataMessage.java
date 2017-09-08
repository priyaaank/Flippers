package org.flippers.messages;

import com.google.protobuf.InvalidProtocolBufferException;
import org.flippers.agent.inbound.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.InetAddress;

import static org.flippers.agent.inbound.MessageListener.DEFAULT_PORT;

public class DataMessage {

    static Logger LOGGER = LoggerFactory.getLogger(DataMessage.class);
    private Integer sourcePort;
    private Long sequenceNumber;
    private InetAddress inetAddress;
    private MessageType messageType;
    private Integer destinationPort;

    public DataMessage(DatagramPacket packet) {
        this.inetAddress = packet.getAddress();
        this.sourcePort = DEFAULT_PORT;
        populateFrom(decode(packet));
    }

    public DataMessage(Long sequenceNumber, InetAddress address, MessageType messageType, Integer destinationPort, Integer sourcePort) {
        this.sequenceNumber = sequenceNumber;
        this.inetAddress = address;
        this.messageType = messageType;
        this.destinationPort = destinationPort;
        this.sourcePort = sourcePort;
    }

    public DatagramPacket toDatagramPacket() {
        MessageProtos.Message message = MessageProtos.Message.newBuilder().
                setSequenceNumber(this.sequenceNumber)
                .setType(MessageProtos.Message.MessageType.valueOf(this.messageType.toString()))
                .setListenPort(this.sourcePort)
                .build();
        byte[] rawData = message.toByteArray();
        return new DatagramPacket(rawData, rawData.length, this.inetAddress, this.destinationPort);
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    private void populateFrom(MessageProtos.Message decodedMessage) {
        if (decodedMessage == null) return;
        this.sequenceNumber = decodedMessage.getSequenceNumber();
        this.messageType = MessageType.valueOf(decodedMessage.getType().toString());
        this.destinationPort = decodedMessage.getListenPort();
    }

    private MessageProtos.Message decode(DatagramPacket packet) {
        try {
            byte[] extractedData = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, extractedData, 0, packet.getLength());
            MessageProtos.Message message = MessageProtos.Message.parseFrom(extractedData);
            return message;
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

}
