package org.apache.camel.kafkaconnector.holmes.model.impl;

import org.apache.camel.kafkaconnector.holmes.model.JsonBuilder;
import org.apache.camel.kafkaconnector.holmes.util.LoggingUtils;
import org.json.JSONObject;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class InitializeMessageContext implements JsonBuilder {
  private String bussObjType;
  private String sender;
  private String receiver;
  private String interfaceID;
  private String scenarioID;
  private String componentType;

  private InitializeMessageContext() {
    //private constructor
  }

  public String getBussObjType() {
    return bussObjType;
  }

  public void setBussObjType(String bussObjType) {
    this.bussObjType = bussObjType;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getInterfaceID() {
    return interfaceID;
  }

  public void setInterfaceID(String interfaceID) {
    this.interfaceID = interfaceID;
  }

  public String getScenarioID() {
    return scenarioID;
  }

  public void setScenarioID(String scenarioID) {
    this.scenarioID = scenarioID;
  }

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  public static Builder newBuilder() {
    return new InitializeMessageContext().new Builder();
  }

  public class Builder {

    private Builder() {
      // private constructor
    }

    public Builder bussObjType(String bussObjType) {
      InitializeMessageContext.this.bussObjType = bussObjType;
      return this;
    }

    public Builder sender(String sender) {
      InitializeMessageContext.this.sender = sender;
      return this;
    }

    public Builder receiver(String receiver) {
      InitializeMessageContext.this.receiver = receiver;
      return this;
    }

    public Builder interfaceID(String interfaceID) {
      InitializeMessageContext.this.interfaceID = interfaceID;
      return this;
    }

    public Builder scenarioID(String scenarioID) {
      InitializeMessageContext.this.scenarioID = scenarioID;
      return this;
    }

    public Builder componentType(String componentType) {
      InitializeMessageContext.this.componentType = componentType;
      return this;
    }

    public InitializeMessageContext build() {
      return InitializeMessageContext.this;
    }

  }

  @Override
  public JSONObject toJsonObject() {
    return new JSONObject()
        .put(LoggingUtils.BUSS_OBJ_TYPE, Objects.toString(bussObjType, EMPTY))
        .put(LoggingUtils.SENDER, Objects.toString(sender, EMPTY))
        .put(LoggingUtils.RECEIVER, Objects.toString(receiver, EMPTY))
        .put(LoggingUtils.INTERFACE_ID, Objects.toString(interfaceID, EMPTY))
        .put(LoggingUtils.SCENARIO_ID, Objects.toString(scenarioID, EMPTY))
        .put(LoggingUtils.COMPONENT_TYPE, Objects.toString(componentType, EMPTY));
  }

  @Override
  public String toString() {
    return "InitializeMessageContext{" +
        "bussObjType='" + bussObjType + '\'' +
        ", sender='" + sender + '\'' +
        ", receiver='" + receiver + '\'' +
        ", interfaceID='" + interfaceID + '\'' +
        ", scenarioID='" + scenarioID + '\'' +
        ", componentType='" + componentType + '\'' +
        '}';
  }
}
