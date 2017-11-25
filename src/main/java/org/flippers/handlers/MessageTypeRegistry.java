package org.flippers.handlers;

import org.flippers.agent.MessageSender;
import org.flippers.messages.MessageType;
import org.flippers.config.FileConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageTypeRegistry {

    private Map<MessageType, Handler> commandHandlerMapping;

    public MessageTypeRegistry(MessageSender sender, FileConfig config) {
        this.commandHandlerMapping = new ConcurrentHashMap<>();
        this.commandHandlerMapping.put(MessageType.PING, new PingHandler(sender, config));
        this.commandHandlerMapping.put(MessageType.ACK, new NoOpHandler());
    }

    public Handler handlerForType(MessageType type) {
        return commandHandlerMapping.get(type);
    }

}
