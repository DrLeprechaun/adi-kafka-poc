package org.adikafka.poc;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class CustomSourceConnectorConfig extends AbstractConfig {

    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic name";

    public CustomSourceConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public CustomSourceConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                .define(TOPIC_CONFIG, ConfigDef.Type.STRING, "input", ConfigDef.Importance.HIGH, TOPIC_DOC);
    }
}
