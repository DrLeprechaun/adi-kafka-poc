package com.adikafka.poc.springbootcamelsink.holmes.model.impl;

import org.apache.camel.kafkaconnector.holmes.model.JsonBuilder;
import org.json.JSONObject;

public class Service implements JsonBuilder {
  private String id;
  private String ephemeralId;
  private String version;

  private Service() {
    //private constructor
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEphemeralId() {
    return ephemeralId;
  }

  public void setEphemeralId(String ephemeralId) {
    this.ephemeralId = ephemeralId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public static Builder newBuilder() {
    return new Service().new Builder();
  }

  public class Builder {

    private Builder() {
      // private constructor
    }

    public Builder id(String id) {
      Service.this.id = id;
      return this;
    }

    public Builder ephemeralId(String ephemeralId) {
      Service.this.ephemeralId = ephemeralId;
      return this;
    }

    public Builder version(String version) {
      Service.this.version = version;
      return this;
    }

    public Service build() {
      return Service.this;
    }
  }

  @Override
  public JSONObject toJsonObject() {
    return new JSONObject()
        .put("id", id)
        .put("ephemeral_id", ephemeralId)
        .put("version", version);
  }

  @Override
  public String toString() {
    return "Service{" +
        "id='" + id + '\'' +
        ", ephemeralId='" + ephemeralId + '\'' +
        ", version='" + version + '\'' +
        '}';
  }
}
