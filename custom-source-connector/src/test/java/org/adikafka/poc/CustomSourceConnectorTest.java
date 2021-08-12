package org.adikafka.poc;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomSourceConnectorTest {

    private CustomSourceConnector customSourceConnector;

    @BeforeEach
    public void openMocks() {
        this.customSourceConnector = new CustomSourceConnector();
    }

    @Test
    public void taskClassTest() {
        assertEquals(customSourceConnector.taskClass(), CustomSourceConnectorTask.class);
    }

    @Test
    public void taskConfigsTest() {
        String newConfigKey = "newConfigKey";
        String newConfigValue = "newConfigValue";
        int maxTasks = 5;

        customSourceConnector.start(ImmutableMap.of(newConfigKey, newConfigValue));
        List<Map<String, String>> taskConfigs = customSourceConnector.taskConfigs(maxTasks);

        assertNotNull(taskConfigs);
        assertEquals(taskConfigs.size(), maxTasks);
        for (Map<String, String> map : taskConfigs) {
            assertEquals(map.get(newConfigKey), newConfigValue);
        }
    }

    @Test
    public void configTest() {
        Map<String, ConfigDef.ConfigKey> configKeys = customSourceConnector.config().configKeys();
        assertEquals(configKeys.size(), 1);
        assertNotNull(configKeys.get("topic"));
    }

    @Test
    public void versionTest() {
        assertEquals(customSourceConnector.version(), CustomSourceConnector.VERSION);
    }
}
