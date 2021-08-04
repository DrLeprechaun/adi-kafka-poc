package com.adikafka.poc.springbootcamelsink.holmes.model;

public interface LogMessage extends JsonBuilder {
  String getTypeName();
}
