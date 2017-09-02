package org.flippers.commands;

import org.flippers.messages.DataMessage;
import org.flippers.agent.inbound.MessageType;
import org.flippers.agent.outbound.FlipperEventLoop;
import org.flippers.tasks.AckResponseTask;

public class PingHandler implements Command {

    private FlipperEventLoop eventLoop;

    public PingHandler(FlipperEventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    @Override
    public void handle(DataMessage dataMessage) {
        DataMessage ackMessage = new DataMessage(
                dataMessage.getSequenceNumber(),
                dataMessage.getInetAddress(),
                MessageType.PING_ACK,
                dataMessage.getDestinationPort()
        );
        this.eventLoop.enqueue(new AckResponseTask(ackMessage));
    }
}
