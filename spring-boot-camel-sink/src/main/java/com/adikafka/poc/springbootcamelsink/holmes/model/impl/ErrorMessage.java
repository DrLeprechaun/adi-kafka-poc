package com.adikafka.poc.springbootcamelsink.holmes.model.impl;

import com.google.common.collect.Lists;
import org.apache.camel.kafkaconnector.holmes.model.Attribute;
import org.apache.camel.kafkaconnector.holmes.model.LogMessage;
import org.apache.camel.kafkaconnector.holmes.util.LoggingUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Objects;

import static org.apache.camel.kafkaconnector.holmes.util.LoggingUtils.buildMessageContext;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ErrorMessage implements LogMessage {
  private String corrID;
  private String bussObjID;
  private String timestamp;
  private String stackTrace;
  private String fullClass;
  private boolean recoverable;
  private Collection<Attribute> logAttributes = Lists.newArrayList();

  private ErrorMessage() {
    //private constructor
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

  public String getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }

  public String getFullClass() {
    return fullClass;
  }

  public void setFullClass(String fullClass) {
    this.fullClass = fullClass;
  }

  public boolean isRecoverable() {
    return recoverable;
  }

  public void setRecoverable(boolean recoverable) {
    this.recoverable = recoverable;
  }

  public Collection<Attribute> getLogAttributes() {
    return logAttributes;
  }

  public void setLogAttributes(Collection<Attribute> logAttributes) {
    this.logAttributes = logAttributes;
  }

  public static Builder newBuilder() {
    return new ErrorMessage().new Builder();
  }

  public class Builder {

    private Builder() {
      // private constructor
    }

    public Builder corrID(String corrID) {
      ErrorMessage.this.corrID = corrID;
      return this;
    }

    public Builder bussObjID(String bussObjID) {
      ErrorMessage.this.bussObjID = bussObjID;
      return this;
    }

    public Builder timestamp(String timestamp) {
      ErrorMessage.this.timestamp = timestamp;
      return this;
    }

    public Builder timestampNow() {
      return timestamp(OffsetDateTime.now().toString());
    }

    public Builder recoverable(boolean recoverable) {
      ErrorMessage.this.recoverable = recoverable;
      return this;
    }

    public Builder stackTrace(String stackTrace) {
      ErrorMessage.this.stackTrace = stackTrace;
      return this;
    }

    public Builder stackTrace(Throwable ex) {
      return stackTrace(ExceptionUtils.getStackTrace(ex));
    }

    public Builder fullClass(String fullClass) {
      ErrorMessage.this.fullClass = fullClass;
      return this;
    }

    public Builder fullClass(Class clazz) {
      return fullClass(clazz.getCanonicalName());
    }

    public Builder logAttribute(String name, String value) {
      ErrorMessage.this.logAttributes.add(new Attribute(name, value));
      return this;
    }

    public Builder logAttributes(Collection<Attribute> attributes) {
      ErrorMessage.this.logAttributes.addAll(attributes);
      return this;
    }

    public ErrorMessage build() {
      return ErrorMessage.this;
    }
  }


  @Override
  public String getTypeName() {
    return LoggingUtils.ERROR_MESSAGE;
  }

  @Override
  public JSONObject toJsonObject() {
    return new JSONObject()
        .put(LoggingUtils.MSG_CTX, buildMessageContext(corrID, bussObjID, timestamp, logAttributes))
        .put(LoggingUtils.PAYLOAD, new JSONObject()
            .put(LoggingUtils.ERROR_TYPE, new JSONObject()
                .put(LoggingUtils.RECOVERABLE, recoverable)
            )
            .put(LoggingUtils.ERROR_REPORT, new JSONObject()
                .put(LoggingUtils.STACK_TRACE, Objects.toString(stackTrace, EMPTY))
                .put(LoggingUtils.FULL_CLASS, Objects.toString(fullClass, EMPTY))
            )
        );
  }

  @Override
  public String toString() {
    return "ErrorMessage{" +
        "corrID='" + corrID + '\'' +
        ", bussObjID='" + bussObjID + '\'' +
        ", timestamp='" + timestamp + '\'' +
        ", stackTrace='" + stackTrace + '\'' +
        ", fullClass='" + fullClass + '\'' +
        ", recoverable=" + recoverable +
        ", logAttributes=" + logAttributes +
        '}';
  }
}
