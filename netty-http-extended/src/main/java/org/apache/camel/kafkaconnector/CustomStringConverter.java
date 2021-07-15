package org.apache.camel.kafkaconnector;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.reporters.InMemoryReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.spi.Reporter;
import io.jaegertracing.spi.Sampler;
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
import com.google.common.collect.ImmutableMap;

import io.opentracing.Tracer;

import java.util.HashMap;
import java.util.Map;

public class CustomStringConverter implements Converter, HeaderConverter {

    private final StringSerializer serializer = new StringSerializer();
    private final StringDeserializer deserializer = new StringDeserializer();

    //Tracer tracer;

    public CustomStringConverter() {
        //this.tracer = getTracer();
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
        System.out.println("+++ FROM CONNECT DATA");
        try {
            /*Reporter reporter = new InMemoryReporter();
            Sampler sampler = new ConstSampler(true);
            Tracer tracer = new JaegerTracer.Builder("adidas-poc")
                    .withReporter(reporter)
                    .withSampler(sampler)
                    .build();
            Span span = tracer.buildSpan("fromConnectData").start();
            span.setTag("fromConnectData", "fromConnectData-tag");
            span.log(ImmutableMap.of("fromConnectData-log", "fromConnectData-log-message"));
            span.finish();*/
            return serializer.serialize(topic, value == null ? null : value.toString());
        } catch (SerializationException e) {
            throw new DataException("Failed to serialize to a string: ", e);
        }
    }

    @Override
    public SchemaAndValue toConnectData(String topic, byte[] value) {
        System.out.println("+++ TO CONNECT DATA");
        try {
            /*Reporter reporter = new InMemoryReporter();
            Sampler sampler = new ConstSampler(true);
            Tracer tracer = new JaegerTracer.Builder("adidas-poc")
                    .withReporter(reporter)
                    .withSampler(sampler)
                    .build();
            Span span = tracer.buildSpan("toConnectData").start();
            span.setTag("toConnectData", "toConnectData-tag");
            span.log(ImmutableMap.of("toConnectData-log", "toConnectData-log-message"));
            span.finish();*/
            return new SchemaAndValue(Schema.OPTIONAL_STRING_SCHEMA, deserializer.deserialize(topic, value));
        } catch (SerializationException e) {
            throw new DataException("Failed to deserialize string: ", e);
        }
    }

    @Override
    public byte[] fromConnectHeader(String topic, String headerKey, Schema schema, Object value) {
        System.out.println("+++ FROM CONNECT HEADER");
        return fromConnectData(topic, schema, value);
    }

    @Override
    public SchemaAndValue toConnectHeader(String topic, String headerKey, byte[] value) {
        System.out.println("+++ TO CONNECT HEADER");
        return toConnectData(topic, value);
    }

    @Override
    public void close() {
        System.out.println("+++ CLOSE");
        // do nothing
    }

    /*private JaegerTracer getTracer() {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
        Configuration config = new Configuration("jaeger tutorial").withSampler(samplerConfig).withReporter(reporterConfig);
        return config.getTracer();

        //JaegerTracer tracer = Tracing.init("hello-world")
    }*/
}
