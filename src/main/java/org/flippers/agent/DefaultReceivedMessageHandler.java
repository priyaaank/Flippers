package org.flippers.agent;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class DefaultReceivedMessageHandler implements ReceivedMessageHandler {

    private static final int DEFAULT_POOL_SIZE = 1;
    static Logger LOGGER = LoggerFactory.getLogger(DefaultReceivedMessageHandler.class);
    private ExecutorService executorService;
    private MessageTypeRegistry registry;

    public DefaultReceivedMessageHandler(ExecutorService executorService, MessageTypeRegistry registry) {
        this.executorService = executorService;
        this.registry = registry;
    }

    @Override
    public void handle(DataMessage message) {
        executorService.submit(() -> this.registry.handlerForType(message.getMessageType()).handle(message));
    }
}
