package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.config.Config;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageCreator;
import org.flippers.peers.MembershipList;

public class JoinHandler implements Handler {

    private final MessageSender sender;
    private final MembershipList membershipList;
    private final MessageCreator messageCreator;

    public JoinHandler(MessageSender sender, MembershipList membershipList, Config config) {
        this.sender = sender;
        this.membershipList = membershipList;
        this.messageCreator = new MessageCreator(config);
    }

    @Override
    public void handle(DataMessage dataMessage) {
        membershipList.forNode(dataMessage.getSourceNode()).initJoining();
        sender.send(messageCreator.ackResponseForJoinMsg(dataMessage));
    }

}
