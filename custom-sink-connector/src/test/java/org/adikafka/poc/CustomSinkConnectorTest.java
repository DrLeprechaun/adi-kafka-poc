package org.adikafka.poc;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomSinkConnectorTest {

    private CustomSinkConnector customSinkConnector;

    @BeforeEach
    public void openMocks() {
        this.customSinkConnector = new CustomSinkConnector();
    }

    @Test
    public void taskClassTest() {
        assertEquals(customSinkConnector.taskClass(), CustomSinkConnectorTask.class);
    }

    @Test
    public void taskConfigsTest() {
        String newConfigKey = "newConfigKey";
        String newConfigValue = "newConfigValue";
        int maxTasks = 5;

        customSinkConnector.start(ImmutableMap.of(newConfigKey, newConfigValue));
        List<Map<String, String>> taskConfigs = customSinkConnector.taskConfigs(maxTasks);

        assertNotNull(taskConfigs);
        assertEquals(taskConfigs.size(), maxTasks);
        for (Map<String, String> map : taskConfigs) {
            assertEquals(map.get(newConfigKey), newConfigValue);
        }
    }

    @Test
    public void configTest() {
        Map<String, ConfigDef.ConfigKey> configKeys = customSinkConnector.config().configKeys();
        assertEquals(configKeys.size(), 1);
        assertNotNull(configKeys.get("topic"));
    }

    @Test
    public void versionTest() {
        assertEquals(customSinkConnector.version(), CustomSinkConnector.VERSION);
    }
}
