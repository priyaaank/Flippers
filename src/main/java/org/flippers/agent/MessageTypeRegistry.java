package org.flippers.agent;

import org.flippers.commands.Command;
import org.flippers.commands.NoOp;
import org.flippers.commands.PingHandler;
import org.flippers.config.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageTypeRegistry {

    private Map<MessageType, Command> commandHandlerMapping;

    public MessageTypeRegistry(MessageSender sender, Config config) {
        this.commandHandlerMapping = new ConcurrentHashMap<>();
        this.commandHandlerMapping.put(MessageType.PING, new PingHandler(sender, config));
        this.commandHandlerMapping.put(MessageType.ACK, new NoOp());
    }

    public Command handlerForType(MessageType type) {
        return commandHandlerMapping.get(type);
    }

}
