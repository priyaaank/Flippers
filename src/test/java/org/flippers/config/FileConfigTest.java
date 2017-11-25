package org.flippers.config;

import org.junit.Before;
import org.junit.Test;

import static org.flippers.config.Config.KeyNames.LISTEN_PORT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FileConfigTest {

    public static final String FLIPPER_CONFIG_OVERRIDE_PROPERTIES = "flipper.config.override.properties";
    FileConfig overriddenConfig;
    FileConfig config;

    @Before
    public void setUp() throws Exception {
        this.overriddenConfig = new FileConfig(FLIPPER_CONFIG_OVERRIDE_PROPERTIES);
        this.config = new FileConfig();
    }

    @Test
    public void shouldReturnLoadedIntegerValueForRequestedKeyNameWhenPresent() throws Exception {
        Integer port = this.config.getValue(LISTEN_PORT, 8822);

        assertThat(port, is(8000));
    }

    @Test
    public void shouldReturnLoadedStringValueForRequestedKeyNameWhenPresent() throws Exception {
        String appName = this.config.getValue("flippers.app.name", "MagicYellow");

        assertThat(appName, is("FlipperTest"));
    }

    @Test
    public void shouldReturnLoadedDoubleValueForRequestedKeyNameWhenPresent() throws Exception {
        Double testAmount = this.config.getValue("flippers.test.amount", 12.0);

        assertThat(testAmount, is(18.0));
    }

    @Test
    public void shouldReturnDefaultStringValueWhenPropertyIsNotPresent() throws Exception {
        String defaultAppName = "YellowApp";
        String nonExistentName = this.config.getValue("yellow.app.name", defaultAppName);

        assertThat(nonExistentName, is(defaultAppName));
    }

    @Test
    public void shouldReturnDefaultIntegerValueWhenPropertyIsNotPresent() throws Exception {
        Integer defaultPort = 80;
        Integer nonExistentPort = this.config.getValue("yellow.listen.port", defaultPort);

        assertThat(nonExistentPort, is(defaultPort));
    }

    @Test
    public void shouldReturnDefaultDoubleValueWhenPropertyIsNotPresent() throws Exception {
        Double defaultAmount = 80.0;
        Double nonExistentAmount = this.config.getValue("yellow.test.amount", defaultAmount);

        assertThat(nonExistentAmount, is(defaultAmount));
    }

    @Test
    public void shouldLoadPropertiesFromSpecifiedFileName() throws Exception {
        String appName = "YellowApp";
        String nonExistentName = this.overriddenConfig.getValue("flippers.app.name", appName);

        assertThat(nonExistentName, is("OverrideApp"));
    }
}