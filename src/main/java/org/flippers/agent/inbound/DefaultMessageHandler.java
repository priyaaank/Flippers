package org.flippers.agent.inbound;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultMessageHandler implements MessageHandler {

    private static final int DEFAULT_POOL_SIZE = 1;
    static Logger LOGGER = LoggerFactory.getLogger(DefaultMessageHandler.class);
    private ExecutorService executorService;

    public DefaultMessageHandler(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public DefaultMessageHandler() {
        this(Executors.newFixedThreadPool(DEFAULT_POOL_SIZE));
    }

    @Override
    public void handle(DataMessage message) {
        executorService.submit(() -> {
            try {
                message.getMessageType().handler().handle(message);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                LOGGER.error(e.getMessage());
            }
        });
    }
}
