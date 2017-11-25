package org.flippers.config;

public interface Config {

    interface KeyNames {

        String LISTEN_PORT = "flipper.listening.port";
        String ACK_TIMEOUT_MILLISECONDS = "flipper.direct.ack.timeout.milliseconds";
        String INDIRECT_ACK_TIMEOUT_MILLISECONDS = "flipper.indirect.ack.timeout.milliseconds";
        String FAILURE_HOLD_OFF_MILLISECONDS = "flipper.failure.suspect.threshold.milliseconds";

    }

    interface DefaultValues {

        Integer DEFAULT_LISTEN_PORT = 8383;
        Integer DEFAULT_ACK_TIMEOUT_MILLISECONDS = 5000;
        Integer DEFAULT_INDIRECT_ACK_TIMEOUT_MILLISECONDS = 5000;
        Integer DEFAULT_FAILURE_HOLD_OFF_MILLISECONDS = 5000;

    }

    String getValue(String keyName, String defaultValue);

    Integer getValue(String keyName, Integer defaultValue);

    Double getValue(String keyName, Double defaultValue);

}
