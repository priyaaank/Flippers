package org.flippers.peers;

import org.flippers.agent.MessageSender;
import org.flippers.config.Config;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Arrays;

import static org.flippers.config.Config.DefaultValues.DEFAULT_SEED_NODES;
import static org.flippers.config.Config.KeyNames.SEED_NODES;

public class MembershipRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipRegistrar.class);
    private static final String HOST_PORT_DELIMITER = ":";
    private static final String EMPTY_STRING = "";
    private final Config config;
    private final MessageSender sender;
    private MessageCreator messageCreator;

    public MembershipRegistrar(Config config, MessageSender sender, MessageCreator messageCreator) {
        this.config = config;
        this.sender = sender;
        this.messageCreator = messageCreator;
    }

    public void initiateRegistration() {
        String[] seedNodes = this.config.getValues(SEED_NODES, DEFAULT_SEED_NODES);
        Arrays.stream(seedNodes)
                .map(String::trim)
                .map(node -> node.split(HOST_PORT_DELIMITER, 2))
                .forEach(node -> this.sendJoinMessage(node[0], node.length > 1 ? node[1] : EMPTY_STRING));
    }

    private void sendJoinMessage(String ipAddress, String port) {
        DataMessage dataMessage;
        try {
            dataMessage = this.messageCreator.craftJoinMessage(PeerNode.nodeFor(ipAddress, port));
            sender.send(dataMessage);
        } catch (UnknownHostException e) {
            LOGGER.error("Could not find host at ip {} and port {}. Exception {}", ipAddress, port, e.getMessage());
        }
    }
}
