package org.apache.camel.kafkaconnector.holmes.model;

public interface LogMessage extends JsonBuilder {
  String getTypeName();
}
