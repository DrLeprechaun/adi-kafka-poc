package org.apache.camel.kafkaconnector.holmes.model.impl;

import org.apache.camel.kafkaconnector.holmes.model.JsonBuilder;
import org.json.JSONObject;

import java.util.Optional;
import java.util.stream.Stream;

public enum Label implements JsonBuilder {
  DEV("dev"),
  SIT("sit"),
  UAT("uat"),
  STG("stg"),
  TEST("test"),
  QA("qa"),
  PRD("prd"),
  UNKNOWN("UNKNOWN");

  private String env;

  Label(String env) {
    this.env = env;
  }

  public String getEnv() {
    return env;
  }

  @Override
  public JSONObject toJsonObject() {
    return new JSONObject()
        .put("env", env);
  }

  @Override
  public String toString() {
    return "Label{" +
        "env='" + env + '\'' +
        '}';
  }

  public static Optional<Label> findLabelByName(String name) {
    return Stream.of(values())
        .filter(label -> label.getEnv().equalsIgnoreCase(name))
        .findFirst();
  }
}
