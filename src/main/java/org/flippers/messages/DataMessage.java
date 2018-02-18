package org.flippers.messages;

import com.google.protobuf.InvalidProtocolBufferException;
import org.flippers.peers.PeerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlType;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class DataMessage {

    static Logger LOGGER = LoggerFactory.getLogger(DataMessage.class);
    static Integer DEFAULT_LISTEN_PORT = 8383;
    private Integer sourcePort;
    private String sequenceNumber;
    private InetAddress sourceAddress;
    private MessageType messageType;
    private Integer destinationPort;

    public  DataMessage(String sequenceNumber, InetAddress address, MessageType messageType, Integer destinationPort, Integer sourcePort) {
        this.sequenceNumber = sequenceNumber;
        this.sourceAddress = address;
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
        return new DatagramPacket(rawData, rawData.length, this.sourceAddress, this.destinationPort);
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public InetAddress getSourceAddress() {
        return sourceAddress;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    public PeerNode getSourceNode() {
        return new PeerNode(this.sourceAddress, this.sourcePort);
    }

    public static DataMessage fromReceivedDatagram(DatagramPacket datagramPacket) {
        if(datagramPacket == null) return null;

        MessageProtos.Message decodedMessage = decode(datagramPacket);

        if(decodedMessage == null) return null;

        return new DataMessage(decodedMessage.getSequenceNumber(),
                datagramPacket.getAddress(),
                MessageType.valueOf(decodedMessage.getType().toString()),
                DEFAULT_LISTEN_PORT,
                decodedMessage.getListenPort());
    }

    private static MessageProtos.Message decode(DatagramPacket packet) {
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
