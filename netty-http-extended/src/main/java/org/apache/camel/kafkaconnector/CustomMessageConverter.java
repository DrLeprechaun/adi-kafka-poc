package org.apache.camel.kafkaconnector;

import com.google.common.collect.ImmutableMap;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.storage.Converter;
import org.apache.kafka.connect.storage.ConverterType;
import org.apache.kafka.connect.storage.HeaderConverter;
import org.apache.kafka.connect.storage.StringConverterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class CustomMessageConverter implements Converter, HeaderConverter {

    private final StringSerializer serializer = new StringSerializer();
    private final StringDeserializer deserializer = new StringDeserializer();

    private static final Logger LOG = LoggerFactory.getLogger(CustomMessageConverter.class);

    public CustomMessageConverter() {
    }

    @Override
    public ConfigDef config() {
        return StringConverterConfig.configDef();
    }

    @Override
    public void configure(Map<String, ?> configs) {
        StringConverterConfig conf = new StringConverterConfig(configs);
        String encoding = conf.encoding();

        Map<String, Object> serializerConfigs = new HashMap<>(configs);
        Map<String, Object> deserializerConfigs = new HashMap<>(configs);
        serializerConfigs.put("serializer.encoding", encoding);
        deserializerConfigs.put("deserializer.encoding", encoding);

        boolean isKey = conf.type() == ConverterType.KEY;
        serializer.configure(serializerConfigs, isKey);
        deserializer.configure(deserializerConfigs, isKey);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Map<String, Object> conf = new HashMap<>(configs);
        conf.put(StringConverterConfig.TYPE_CONFIG, isKey ? ConverterType.KEY.getName() : ConverterType.VALUE.getName());
        configure(conf);
    }

    @Override
    public byte[] fromConnectData(String topic, Schema schema, Object value) {
        try {
            return serializer.serialize(topic, value == null ? null : value.toString());
        } catch (SerializationException e) {
            throw new DataException("Failed to serialize to a string: ", e);
        }
    }

    @Override
    public SchemaAndValue toConnectData(String topic, byte[] value) {
        LOG.info("+++ Input message byte array length: " + value.length);
        try {
            SchemaAndValue schemaAndValue = new SchemaAndValue(Schema.OPTIONAL_STRING_SCHEMA, deserializer.deserialize(topic, value));
            LOG.info("+++ Deserialized message length: " + schemaAndValue.value().toString().length());
            LOG.info("+++ Deserialized message schema name: " + schemaAndValue.schema().name());
            LOG.info("+++ Deserialized message schema type: " + schemaAndValue.schema().type().getName());
            LOG.info("+++ Is deserialized message schema optional: " + schemaAndValue.schema().isOptional());

            //Jaeger tracing
            Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
            Configuration.SenderConfiguration senderConfig = Configuration.SenderConfiguration.fromEnv().withAgentHost("host.docker.internal");
            Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true).withSender(senderConfig);
            Configuration config = new Configuration("adidas-poc").withSampler(samplerConfig).withReporter(reporterConfig);
            JaegerTracer tracer = config.getTracer();

            MDC.put("tracer-metrics", tracer.getMetrics().toString());
            LOG.info("+++ tracer-metrics: " + MDC.get("tracer-metrics"));
            MDC.remove("tracer-metrics");

            Span span = tracer.buildSpan("adidas-poc-span").start();
            span.setTag("adidas-poc-tag", "Some tag value");
            span.setBaggageItem("adidas-poc-baggage-item", "baggage-item-value");
            span.log(ImmutableMap.of("Adidas PoC log", "This is the log sample",
                    "Custom field", "And this is just custom field value",
                    "message", schemaAndValue.value().toString()));
            span.finish();

            return  schemaAndValue;
            //return new SchemaAndValue(Schema.OPTIONAL_STRING_SCHEMA, deserializer.deserialize(topic, value));
        } catch (SerializationException e) {
            throw new DataException("Failed to deserialize string: ", e);
        }
    }

    @Override
    public byte[] fromConnectHeader(String topic, String headerKey, Schema schema, Object value) {
        return fromConnectData(topic, schema, value);
    }

    @Override
    public SchemaAndValue toConnectHeader(String topic, String headerKey, byte[] value) {
        return toConnectData(topic, value);
    }

    @Override
    public void close() {
        // do nothing
    }
}
