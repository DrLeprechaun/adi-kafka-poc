package org.adikafka.poc;

import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomSourceConnectorConfigTest {

    @Test
    public void confTest() {
        ConfigDef configDef = CustomSourceConnectorConfig.conf();
        assertEquals(configDef.configKeys().size(), 1);
        assertNotNull(configDef.configKeys().get("topic"));
    }
}
