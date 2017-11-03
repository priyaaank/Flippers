package org.flippers.membership;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MembershipList {

    private static final Integer START_INDEX = 0;
    private List<PeerNode> registeredMembers;

    public MembershipList() {
        this.registeredMembers = new ArrayList<>();
    }

    public List<PeerNode> selectNodesRandomly(Integer selectionCount) {
        if (registeredMembers == null || registeredMembers.size() == 0) return null;
        if (selectionCount >= registeredMembers.size()) throw new RuntimeException("Registered members are less than requested member count");
        return randomlySelectNodes(selectionCount);
    }

    private List<PeerNode> randomlySelectNodes(Integer selectionCount) {
        return new Random().ints(selectionCount, START_INDEX, registeredMembers.size())
                .mapToObj(registeredMembers::get)
                .collect(Collectors.toList());
    }

    public void add(PeerNode peerNode) {
        this.registeredMembers.add(peerNode);
    }
}
