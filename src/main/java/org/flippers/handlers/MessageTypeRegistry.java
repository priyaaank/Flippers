package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.messages.MessageCreator;
import org.flippers.messages.MessageType;
import org.flippers.config.FileConfig;
import org.flippers.peers.MembershipList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageTypeRegistry {

    private Map<MessageType, Handler> commandHandlerMapping;

    public MessageTypeRegistry(MessageSender sender, FileConfig config, MembershipList membershipList, MessageCreator messageCreator) {
        this.commandHandlerMapping = new ConcurrentHashMap<>();
        this.commandHandlerMapping.put(MessageType.PING, new PingHandler(sender, messageCreator));
        this.commandHandlerMapping.put(MessageType.ACK, new AckHandler(membershipList));
        this.commandHandlerMapping.put(MessageType.JOIN, new JoinHandler(sender, membershipList, messageCreator));
    }

    public Handler handlerForType(MessageType type) {
        return commandHandlerMapping.get(type);
    }

}
