package org.apache.camel.kafkaconnector.holmes.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static org.apache.camel.kafkaconnector.holmes.constant.LoggingConstants.LOGGING_CONFIG_PATH;

public class LoggingConfigProvider {

  private static class LoggingConfigHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingConfigHolder.class);
    private static final LoggingConfig loggingConfig = initLoggingConfig(loadLoggingConfig());

    private static Properties loadLoggingConfig() {
      Properties properties = new Properties();
      try {
        String configPath = System.getProperty(LOGGING_CONFIG_PATH);
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
      }
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
