package org.flippers.agent.inbound;

import org.flippers.messages.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultMessageHandler implements MessageHandler {

    private static final int DEFAULT_POOL_SIZE = 2;
    static Logger LOGGER = LoggerFactory.getLogger(DefaultMessageHandler.class);
    private ExecutorService executorService;

    public DefaultMessageHandler(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public DefaultMessageHandler() {
        this(Executors.newFixedThreadPool(DEFAULT_POOL_SIZE));
    }

    @Override
    public void handle(DataMessage packet) {
        executorService.submit(() -> System.out.print(packet.getMessageType()));
    }
}
