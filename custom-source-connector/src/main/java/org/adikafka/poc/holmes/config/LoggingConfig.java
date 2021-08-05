package org.adikafka.poc.holmes.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.adikafka.poc.holmes.constant.LoggingConstants.LOGGING_UNKNOWN;

public class LoggingConfig {

  private Map<String, String> propertiesMap;

  public LoggingConfig(Properties properties) {
    this.propertiesMap = new HashMap(properties);
  }

  public String getProperty(String key) {
    return propertiesMap.get(key);
  }

  public String getPropertyWithDefault(String key, String defaultValue) {
    return Optional.ofNullable(propertiesMap.get(key)).orElse(defaultValue);
  }

  public String getPropertyWithDefaultUnknown(String key) {
    return getPropertyWithDefault(key, LOGGING_UNKNOWN);
  }

}
