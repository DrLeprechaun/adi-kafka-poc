package org.apache.camel.kafkaconnector.holmes.model.impl;

import com.google.common.collect.Lists;
import org.apache.camel.kafkaconnector.holmes.model.Attribute;
import org.apache.camel.kafkaconnector.holmes.model.AuditType;
import org.apache.camel.kafkaconnector.holmes.model.LogMessage;
import org.apache.camel.kafkaconnector.holmes.util.LoggingUtils;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Objects;

import static org.apache.camel.kafkaconnector.holmes.util.LoggingUtils.buildMessageContext;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AuditMessage implements LogMessage {
  private String corrID;
  private String bussObjID;
  private String timestamp;
  private String additionalMsg;
  private AuditType auditType;
  private Collection<Attribute> logAttributes = Lists.newArrayList();

  private AuditMessage() {
    // private constructor
  }

  public String getCorrID() {
    return corrID;
  }

  public void setCorrID(String corrID) {
    this.corrID = corrID;
  }

  public String getBussObjID() {
    return bussObjID;
  }

  public void setBussObjID(String bussObjID) {
    this.bussObjID = bussObjID;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getAdditionalMsg() {
    return additionalMsg;
  }

  public void setAdditionalMsg(String additionalMsg) {
    this.additionalMsg = additionalMsg;
  }

  public AuditType getAuditType() {
    return auditType;
  }

  public void setAuditType(AuditType auditType) {
    this.auditType = auditType;
  }

  public Collection<Attribute> getLogAttributes() {
    return logAttributes;
  }

  public void setLogAttributes(Collection<Attribute> logAttributes) {
    this.logAttributes = logAttributes;
  }

  public static Builder newBuilder() {
    return new AuditMessage().new Builder();
  }

  public class Builder {

    private Builder() {
      // private constructor
    }

    public Builder corrID(String corrID) {
      AuditMessage.this.corrID = corrID;
      return this;
    }

    public Builder bussObjID(String bussObjID) {
      AuditMessage.this.bussObjID = bussObjID;
      return this;
    }

    public Builder timestamp(String timestamp) {
      AuditMessage.this.timestamp = timestamp;
      return this;
    }

    public Builder timestampNow() {
      return timestamp(OffsetDateTime.now().toString());
    }

    public Builder additionalMsg(String additionalMsg) {
      AuditMessage.this.additionalMsg = additionalMsg;
      return this;
    }

    public Builder auditType(AuditType auditType) {
      AuditMessage.this.auditType = auditType;
      return this;
    }

    public Builder logAttribute(String name, String value) {
      AuditMessage.this.logAttributes.add(new Attribute(name, value));
      return this;
    }

    public Builder logAttributes(Collection<Attribute> attributes) {
      AuditMessage.this.logAttributes.addAll(attributes);
      return this;
    }

    public AuditMessage build() {
      return AuditMessage.this;
    }
  }

  @Override
  public String getTypeName() {
    return LoggingUtils.AUDIT_MESSAGE;
  }

  @Override
  public JSONObject toJsonObject() {
    return new JSONObject()
        .put(LoggingUtils.MSG_CTX, buildMessageContext(corrID, bussObjID, timestamp, logAttributes))
        .put(LoggingUtils.PAYLOAD, new JSONObject()
            .put(LoggingUtils.ADDITIONAL_MSG, Objects.toString(additionalMsg, EMPTY))
            .put(LoggingUtils.AUDIT_TYPE, Objects.toString(auditType, EMPTY))
        );
  }

  @Override
  public String toString() {
    return "AuditMessage{" +
        "corrID='" + corrID + '\'' +
        ", bussObjID='" + bussObjID + '\'' +
        ", timestamp='" + timestamp + '\'' +
        ", additionalMsg='" + additionalMsg + '\'' +
        ", auditType=" + auditType +
        ", logAttributes=" + logAttributes +
        '}';
  }
}
