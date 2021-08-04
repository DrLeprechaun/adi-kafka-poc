package com.adikafka.poc.springbootcamelsink.holmes.util;

import org.apache.camel.kafkaconnector.holmes.model.Attribute;
import org.apache.camel.kafkaconnector.holmes.model.JsonBuilder;
import org.apache.camel.kafkaconnector.holmes.model.impl.InitializeMessageContext;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Objects;

import static org.apache.camel.kafkaconnector.holmes.config.LoggingConfigProvider.getLoggingConfig;
import static org.apache.camel.kafkaconnector.holmes.constant.LoggingConstants.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class LoggingUtils {

  //Common BI/CRM logging fields
  public static final String AUDIT_MESSAGE = "AuditMessage";
  public static final String INITIALIZE_MESSAGE_CONTEXT = "InitializeMessageContext";
  public static final String MSG_CTX = "MsgCtx";
  public static final String CORR_ID = "corrID";
  public static final String BUSS_OBJ_ID = "bussObjID";
  public static final String TIMESTAMP = "timestamp";
  public static final String LOG_ATTRIBUTES = "LogAttributes";
  public static final String PAYLOAD = "Payload";
  public static final String ADDITIONAL_MSG = "AdditionalMsg";
  public static final String AUDIT_TYPE = "AuditType";
  public static final String BUSS_OBJ_TYPE = "bussObjType";
  public static final String SENDER = "sender";
  public static final String RECEIVER = "receiver";
  public static final String INTERFACE_ID = "InterfaceID";
  public static final String SCENARIO_ID = "scenarioID";
  public static final String COMPONENT_TYPE = "componentType";
  public static final String ERROR_MESSAGE = "ErrorMessage";
  public static final String ERROR_TYPE = "ErrorType";
  public static final String RECOVERABLE = "Recoverable";
  public static final String ERROR_REPORT = "ErrorReport";
  public static final String STACK_TRACE = "StackTrace";
  public static final String FULL_CLASS = "FullClass";
  public static final String EVENT_TYPE = "EventType";

  public static final InitializeMessageContext INITIALIZE_MESSAGE_CONTEXT_OBJECT =
      InitializeMessageContext.newBuilder()
          .bussObjType(getLoggingConfig().getPropertyWithDefaultUnknown(LOGGING_BUSSOBJTYPE))
          .sender(getLoggingConfig().getPropertyWithDefaultUnknown(LOGGING_SENDER))
          .receiver(getLoggingConfig().getPropertyWithDefaultUnknown(LOGGING_RECEIVER))
          .interfaceID(getLoggingConfig().getPropertyWithDefaultUnknown(LOGGING_INTERFACEID))
          .scenarioID(getLoggingConfig().getPropertyWithDefaultUnknown(LOGGING_SCENARIOID))
          .componentType(getLoggingConfig().getPropertyWithDefaultUnknown(LOGGING_COMPONENTTYPE))
          .build();

  public static JSONObject buildMessageContext(String corrID, String bussObjID, String timestamp,
                                               Collection<Attribute> logAttributes) {
    return new JSONObject()
        .put(CORR_ID, Objects.toString(corrID, EMPTY))
        .put(BUSS_OBJ_ID, Objects.toString(bussObjID, EMPTY))
        .put(TIMESTAMP, Objects.toString(timestamp, EMPTY))
        .put(LOG_ATTRIBUTES, logAttributes);
  }

  public static JSONObject buildJsonObjectSafely(JsonBuilder jsonBuilder) {
    return Objects.isNull(jsonBuilder)
        ? new JSONObject()
        : jsonBuilder.toJsonObject();
  }

}
