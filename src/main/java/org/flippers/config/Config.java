package org.flippers.config;

public interface Config {

    interface KeyNames {

        String LISTEN_PORT = "flipper.listening.port";
        String ACK_TIMEOUT_MILLISECONDS = "flipper.direct.ack.timeout.milliseconds";
        String INDIRECT_ACK_TIMEOUT_MILLISECONDS = "flipper.indirect.ack.timeout.milliseconds";
        String FAILURE_HOLD_OFF_MILLISECONDS = "flipper.failure.suspect.threshold.milliseconds";
        String THREAD_POOL_SIZE = "flipper.thread.pool.count";
        String FAILURE_DETECTION_INITIAL_DELAY = "flipper.failure.detection.initial.delay.millis";
        String FAILURE_DETECTION_DELAY_PERIOD = "flipper.failure.detection.delay.period.millis";
        String RANDOM_NODE_SELECTION_COUNT = "flipper.dissemination.random.node.selection.count";
        String EVENT_LOG_QUEUE_SIZE = "flipper.dissemination.even.log.queue.size";

    }

    interface DefaultValues {

        Integer DEFAULT_LISTEN_PORT = 8383;
        Integer DEFAULT_ACK_TIMEOUT_MILLISECONDS = 5000;
        Integer DEFAULT_INDIRECT_ACK_TIMEOUT_MILLISECONDS = 5000;
        Integer DEFAULT_FAILURE_HOLD_OFF_MILLISECONDS = 5000;
        Integer DEFAULT_THREAD_POOL_COUNT = 2;
        Integer DEFAULT_FAILURE_DETECTION_INITIAL_DELAY = 1000;
        Integer DEFAULT_FAILURE_DETECTION_DELAY_PERIOD = 1000;
        Integer DEFAULT_RANDOM_NODE_SELECTION_COUNT = 5;
        Integer DEFAULT_EVENT_QUEUE_SIZE = 10000;


    }

    String getValue(String keyName, String defaultValue);

    Integer getValue(String keyName, Integer defaultValue);

    Double getValue(String keyName, Double defaultValue);

}
