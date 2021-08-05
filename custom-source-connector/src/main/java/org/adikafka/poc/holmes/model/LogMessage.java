package org.adikafka.poc.holmes.model;

public interface LogMessage extends JsonBuilder {
  String getTypeName();
}
