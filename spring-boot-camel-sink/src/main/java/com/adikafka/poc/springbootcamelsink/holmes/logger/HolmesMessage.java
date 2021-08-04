package com.adikafka.poc.springbootcamelsink.holmes.logger;

import org.apache.camel.kafkaconnector.holmes.model.impl.HolmesLog;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;

import java.util.Map;

public class HolmesMessage implements Message {

    private JSONObject jsonObject;

    public HolmesMessage(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HolmesMessage(Map messageMap) {
        this.jsonObject = new JSONObject(messageMap);
    }

    public HolmesMessage(HolmesLog holmesLog) {
        this.jsonObject = holmesLog.toJsonObject();
    }

    @Override
    public String getFormattedMessage() {
        return jsonObject.toString();
    }

    @Override
    public String getFormat() {
        return Strings.EMPTY;
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }
}
