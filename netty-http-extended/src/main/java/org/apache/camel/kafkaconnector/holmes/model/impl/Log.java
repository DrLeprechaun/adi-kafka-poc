package org.apache.camel.kafkaconnector.holmes.model.impl;

import org.apache.camel.kafkaconnector.holmes.model.JsonBuilder;
import org.apache.camel.kafkaconnector.holmes.model.Level;
import org.apache.camel.kafkaconnector.holmes.model.LogMessage;
import org.json.JSONObject;

import static org.apache.camel.kafkaconnector.holmes.util.LoggingUtils.buildJsonObjectSafely;


public class Log implements JsonBuilder {
  private Level level;
  private LogMessage logMessage;
  private InitializeMessageContext initializeMessageContext;

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public LogMessage getMessage() {
    return logMessage;
  }

  public void setMessage(LogMessage logMessage) {
    this.logMessage = logMessage;
  }

  public InitializeMessageContext getInitializeMessageContext() {
    return initializeMessageContext;
  }

  public void setInitializeMessageContext(InitializeMessageContext initializeMessageContext) {
    this.initializeMessageContext = initializeMessageContext;
  }

  public static Builder newBuilder() {
    return new Log().new Builder();
  }

  public class Builder {

    private Builder() {
      // private constructor
    }

    public Builder level(Level level) {
      Log.this.level = level;
      return this;
    }

    public Builder logMessage(LogMessage logMessage) {
      Log.this.logMessage = logMessage;
      return this;
    }

    public Builder initializeMessageContext(InitializeMessageContext initializeMessageContext) {
      Log.this.initializeMessageContext = initializeMessageContext;
      return this;
    }

    public Log build() {
      return Log.this;
    }
  }

  @Override
  public JSONObject toJsonObject() {
    return new JSONObject()
        .put("level", level)
        .put(logMessage.getTypeName(), buildJsonObjectSafely(logMessage))
        .put("InitializeMessageContext", buildJsonObjectSafely(initializeMessageContext));
  }

  @Override
  public String toString() {
    return "Log{" +
        "level=" + level +
        ", logMessage=" + logMessage +
        ", initializeMessageContext=" + initializeMessageContext +
        '}';
  }
}
