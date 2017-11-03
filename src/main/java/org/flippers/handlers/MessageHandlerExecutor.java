package org.flippers.handlers;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class MessageHandlerExecutor implements HandlerExecutor {

    private static final int DEFAULT_POOL_SIZE = 1;
    static Logger LOGGER = LoggerFactory.getLogger(MessageHandlerExecutor.class);
    private ExecutorService executorService;
    private MessageTypeRegistry registry;

    public MessageHandlerExecutor(ExecutorService executorService, MessageTypeRegistry registry) {
        this.executorService = executorService;
        this.registry = registry;
    }

    @Override
    public void executeHandler(DataMessage message) {
        executorService.submit(() -> this.registry.handlerForType(message.getMessageType()).handle(message));
    }
}
