package org.flippers.commands;

import org.flippers.messages.DataMessage;
import org.flippers.agent.inbound.MessageType;
import org.flippers.agent.outbound.FlipperEventLoop;
import org.flippers.tasks.AckResponseTask;

import static org.flippers.agent.inbound.MessageListener.DEFAULT_PORT;

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
                MessageType.ACK,
                dataMessage.getSourcePort(),
                DEFAULT_PORT
        );
        this.eventLoop.enqueue(new AckResponseTask(ackMessage));
    }
}
