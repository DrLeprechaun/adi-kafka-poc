package org.adikafka.poc;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CustomSourceConnector extends SourceConnector {

    public static final String VERSION = "1.O.O";
    private static final Logger log = LoggerFactory.getLogger(CustomSourceConnector.class);
    private CustomSourceConnectorConfig config;

    @Override
    public void start(Map<String, String> props) {
        log.info("{} Starting RestSourceConnector", this);
        this.config = new CustomSourceConnectorConfig(props);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return CustomSourceConnectorTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        Map<String, String> taskConfig = new HashMap<>(config.originalsStrings());
        return new ArrayList<>(Collections.nCopies(maxTasks, taskConfig));
    }

    @Override
    public void stop() {
        log.info("{} +++ Stopping CustomSourceConnector.", this);
    }

    @Override
    public ConfigDef config() {
        return CustomSourceConnectorConfig.conf();
    }

    @Override
    public String version() {
        return VERSION;
    }
}
