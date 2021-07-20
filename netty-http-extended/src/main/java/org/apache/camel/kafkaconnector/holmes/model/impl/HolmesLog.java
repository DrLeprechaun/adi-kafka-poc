package org.apache.camel.kafkaconnector.holmes.model.impl;

import org.apache.camel.kafkaconnector.holmes.model.JsonBuilder;
import org.json.JSONObject;

import java.time.OffsetDateTime;

import static org.apache.camel.kafkaconnector.holmes.util.LoggingUtils.buildJsonObjectSafely;

public class HolmesLog implements JsonBuilder {
  private String message;
  private String timestamp;
  private Label labels;
  private Service service;
  private Log log;

  private HolmesLog() {
    //private constructor
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public Label getLabels() {
    return labels;
  }

  public void setLabels(Label labels) {
    this.labels = labels;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public Log getLog() {
    return log;
  }

  public void setLog(Log log) {
    this.log = log;
  }

  public static Builder newBuilder() {
    return new HolmesLog().new Builder();
  }


  public class Builder {

    private Builder() {
      // private constructor
    }

    public Builder message(String message) {
      HolmesLog.this.message = message;
      return this;
    }

    public Builder timestamp(String timestamp) {
      HolmesLog.this.timestamp = timestamp;
      return this;
    }

    public Builder timestampNow() {
      return timestamp(OffsetDateTime.now().toString());
    }

    public Builder labels(Label labels) {
      HolmesLog.this.labels = labels;
      return this;
    }

    public Builder service(Service service) {
      HolmesLog.this.service = service;
      return this;
    }

    public Builder log(Log log) {
      HolmesLog.this.log = log;
      return this;
    }

    public HolmesLog build() {
      return HolmesLog.this;
    }
  }

  @Override
  public JSONObject toJsonObject() {
    return new JSONObject()
        .put("message", message)
        .put("@timestamp", timestamp)
        .put("labels", buildJsonObjectSafely(labels))
        .put("service", buildJsonObjectSafely(service))
        .put("log", buildJsonObjectSafely(log));
  }

  @Override
  public String toString() {
    return "HolmesLog{" +
        "message='" + message + '\'' +
        ", timestamp='" + timestamp + '\'' +
        ", labels=" + labels +
        ", service=" + service +
        ", log=" + log +
        '}';
  }
}
