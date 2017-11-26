package org.flippers.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileConfig implements Config {

    private static String DEFAULT_PROPERTIES_FILE = "flipper.config.default.properties";
    private final Properties properties;
    private final static Logger LOGGER = LoggerFactory.getLogger(FileConfig.class);

    public FileConfig() {
        this(DEFAULT_PROPERTIES_FILE);
    }

    public FileConfig(String fileName) {
        properties = new Properties();
        loadConfig(fileName);
    }
    
    @Override
    public String getValue(String keyName, String defaultValue) {
        return this.properties.getProperty(keyName, defaultValue);
    }

    @Override
    public Integer getValue(String keyName, Integer defaultValue) {
        String property = this.properties.getProperty(keyName);
        return property == null ? defaultValue : Integer.valueOf(property);
    }

    @Override
    public Double getValue(String keyName, Double defaultValue) {
        String property = this.properties.getProperty(keyName);
        return property == null ? defaultValue : Double.valueOf(property);
    }

    private void loadConfig(String fileName) {
        try (InputStream input = FileConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) return;
            properties.load(input);
            LOGGER.debug("Loaded all properties successfully. Count of properties loaded {}", properties.size());
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

}
