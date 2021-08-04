package com.adikafka.poc.springbootcamelsink.holmes.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class LoggingConfigProvider {

    private static class LoggingConfigHolder {

        private static final Logger LOGGER = LoggerFactory.getLogger(LoggingConfigHolder.class);
        private static final LoggingConfig loggingConfig = initLoggingConfig(loadLoggingConfig());

        private static Properties loadLoggingConfig() {
            Properties properties = new Properties();
            properties.setProperty("logging.bussObjType", "Camel Kafka Connect PoC");
            properties.setProperty("logging.sender", "CamelKafkaConnector.Sender");
            properties.setProperty("logging.receiver", "CamelKafkaConnector.Reciever");
            properties.setProperty("logging.InterfaceID", "1234");
            properties.setProperty("logging.scenarioID", "2345");
            properties.setProperty("logging.componentType", "SUB");
            properties.setProperty("logging.ephemeralId", "576c97ef-e6e4-467d-a636-4db1810b7a33");
            properties.setProperty("logging.kafka.version", "Kafka-V1");
            properties.setProperty("logging.environment", "dev");
            return properties;
            /*try {
                String configPath = "logging.properties";
                if (Objects.isNull(configPath)) {
                    LOGGER.warn("Missing 'loggingConfigFile' argument, falling back to default properties");
                    properties.load(getDefaultPropertiesStream());
                } else {
                    properties.load(new FileInputStream(configPath));
                }
            } catch (IOException e) {
                LOGGER.warn("Failed to load logging config file, falling back to default properties");
                properties.load(getDefaultPropertiesStream());
            } finally {
                return properties;
            }*/
        }

        private static LoggingConfig initLoggingConfig(Properties properties) {
            return new LoggingConfig(properties);
        }

        private static InputStream getDefaultPropertiesStream() {
            return LoggingConfigProvider.class.getClassLoader()
                    .getResourceAsStream("logging-default.properties");
        }
    }

    public static LoggingConfig getLoggingConfig() {
        return LoggingConfigHolder.loggingConfig;
    }
}
