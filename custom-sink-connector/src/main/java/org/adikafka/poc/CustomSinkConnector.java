package org.adikafka.poc;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CustomSinkConnector extends SinkConnector {

    public static final String VERSION = "1.O.O";
    private static final Logger log = LoggerFactory.getLogger(CustomSinkConnector.class);
    private CustomSinkConnectorConfig config;

    @Override
    public void start(Map<String, String> props) {
        log.info("{} Starting RestSinkConnector", this);
        this.config = new CustomSinkConnectorConfig(props);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return CustomSinkConnectorTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        Map<String, String> taskConfig = new HashMap<>(config.originalsStrings());
        return new ArrayList<>(Collections.nCopies(maxTasks, taskConfig));
    }

    @Override
    public void stop() {
        log.info("{} +++ Stopping CustomSinkConnector.", this);
    }

    @Override
    public ConfigDef config() {
        return CustomSinkConnectorConfig.conf();
    }

    @Override
    public String version() {
        return VERSION;
    }
}
