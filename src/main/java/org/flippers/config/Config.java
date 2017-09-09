package org.flippers.config;

public class Config {

    private static final Integer DEFAULT_PORT = 8343;
    private Integer listenPort;

    private Config() {

    }

    public Integer getListenPort() {
        return listenPort;
    }

    public static class ConfigBuilder {

        private Config config = new Config();

        public ConfigBuilder() {
            this.config.listenPort = DEFAULT_PORT;
        }

        public ConfigBuilder listenPort(Integer listenPort) {
            this.config.listenPort = listenPort;
            return this;
        }

        public Config build() {
            return config;
        }

    }
}
