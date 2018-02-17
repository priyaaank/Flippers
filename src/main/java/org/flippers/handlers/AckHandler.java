package org.flippers.handlers;

import org.flippers.messages.DataMessage;
import org.flippers.peers.MembershipList;

public class AckHandler implements Handler {

    private MembershipList membershipList;

    public AckHandler(MembershipList membershipList) {
        this.membershipList = membershipList;
    }

    @Override
    public void handle(DataMessage dataMessage) {
        this.membershipList.forNode(dataMessage.getSourceNode()).markAlive();
    }

}
