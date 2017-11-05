package org.flippers.peers.states;

import org.flippers.registry.ObjectRegistry;

public class NodeStateFactory {

    private static final NodeStateFactory factoryInstance = new NodeStateFactory();

    private NodeStateFactory() {
        ObjectRegistry.getInstance().register(new Alive());
        ObjectRegistry.getInstance().register(new AwaitingAck());
        ObjectRegistry.getInstance().register(new AwaitingIndirectAck());
        ObjectRegistry.getInstance().register(new Dead());
        ObjectRegistry.getInstance().register(new Exited());
        ObjectRegistry.getInstance().register(new FailureSuspected());
        ObjectRegistry.getInstance().register(new Joined());
    }

    public static NodeStateFactory getInstance() {
        return factoryInstance;
    }

    public NodeState joinedState() {
        return ObjectRegistry.getInstance().instanceOf(Joined.class);
    }

    public NodeState aliveState() {
        return ObjectRegistry.getInstance().instanceOf(Alive.class);
    }

    public NodeState awaitingAckState() {
        return ObjectRegistry.getInstance().instanceOf(AwaitingAck.class);
    }

    public NodeState awaitingIndirectAckState() {
        return ObjectRegistry.getInstance().instanceOf(AwaitingIndirectAck.class);
    }

    public NodeState deadState() {
        return ObjectRegistry.getInstance().instanceOf(Dead.class);
    }

    public NodeState exitedState() {
        return ObjectRegistry.getInstance().instanceOf(Exited.class);
    }

    public NodeState failureSuspectedState() {
        return ObjectRegistry.getInstance().instanceOf(FailureSuspected.class);
    }
}
