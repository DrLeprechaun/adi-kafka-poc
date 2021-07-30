package org.adikafka.poc;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;

public class CustomSinkConnectorConfig extends AbstractConfig {

    // TODO: topic property and its documentation goes here
    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic name";

    // TODO If needed your getters goes here

    public CustomSinkConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public CustomSinkConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                // TODO Attache your config to ConfigDef
                .define(TOPIC_CONFIG, Type.STRING, "input", Importance.HIGH, TOPIC_DOC);
    }
}
