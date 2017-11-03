package org.flippers.membership;

import java.net.InetAddress;

public class PeerNode {

    private InetAddress ipAddress;
    private Integer port;
    private NodeState state;

    public PeerNode(InetAddress ipAddress, Integer port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.state = NodeState.RUNNING;
    }

    public Integer getPort() {
        return port;
    }

    enum NodeState {
        RUNNING,
        FAILED_SUSPECT,
        FAILED
    }

}
