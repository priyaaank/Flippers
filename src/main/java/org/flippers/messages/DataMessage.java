package org.flippers.messages;

import com.google.protobuf.InvalidProtocolBufferException;
import org.flippers.peers.PeerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

public class DataMessage {

    static Logger LOGGER = LoggerFactory.getLogger(DataMessage.class);
    private String sequenceNumber;
    private PeerNode sourceNode;
    private PeerNode destinationNode;
    private MessageType messageType;

    public DataMessage(String sequenceNumber, PeerNode sourceNode, MessageType messageType, PeerNode destinationNode) {
        this.sequenceNumber = sequenceNumber;
        this.sourceNode = sourceNode;
        this.messageType = messageType;
        this.destinationNode = destinationNode;
    }

    public DataMessage(String sequenceNumber, PeerNode sourceNode, MessageType messageType) {
        this.sequenceNumber = sequenceNumber;
        this.sourceNode = sourceNode;
        this.messageType = messageType;
        this.destinationNode = null;
    }

    public DatagramPacket toDatagramPacket() {
        MessageProtos.Message message = MessageProtos.Message.newBuilder()
                .setSequenceNumber(this.sequenceNumber)
                .setType(MessageProtos.Message.MessageType.valueOf(this.messageType.toString()))
                .setSender(MessageProtos.NodeInfo
                        .newBuilder()
                        .setIpAddress(this.sourceNode.getIpAddress().toString())
                        .setPort(this.sourceNode.getPort())
                        .build())
                .build();
        byte[] rawData = message.toByteArray();
        return new DatagramPacket(rawData, rawData.length, this.destinationNode.getIpAddress(), this.destinationNode.getPort());
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public PeerNode getSourceNode() {
        return sourceNode;
    }

    public PeerNode getDestinationNode() {
        return destinationNode;
    }

    public MessageType getMessageType() {
        return messageType;
    }


    public static DataMessage fromReceivedDatagram(DatagramPacket datagramPacket) throws UnknownHostException {
        if (datagramPacket == null) return null;

        MessageProtos.Message decodedMessage = decode(datagramPacket);

        if (decodedMessage == null) return null;

        return new DataMessage(decodedMessage.getSequenceNumber(),
                PeerNode.nodeFor(datagramPacket.getAddress(), datagramPacket.getPort()),
                MessageType.valueOf(decodedMessage.getType().toString()));
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
