package org.adikafka.poc;

import com.google.common.collect.ImmutableMap;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class CustomSourceConnectorTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(CustomSourceConnectorTask.class);
    public CustomSourceConnectorConfig config;

    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {
        config = new CustomSourceConnectorConfig(props);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        log.info("+++ POLL METHOD STARTS");

        List<SourceRecord> records = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://enj9dtgx4z1d62y.m.pipedream.net")
                .get()
                .build();

        try {
            log.info("+++ EXECUTE REQUEST");
            Response response = client.newCall(request).execute();
            Schema schema = SchemaBuilder.struct()
                    .name("ItemValue")
                    .parameters(Collections.emptyMap())
                    .field("value", Schema.STRING_SCHEMA)
                    .build();
            Struct struct = new Struct(schema).put("value", Objects.requireNonNull(response.body()).toString());
            SourceRecord record = new SourceRecord(Collections.emptyMap(), Collections.emptyMap(), "output", schema, struct);
            records.add(record);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public void stop() {
        log.info("{} Stopping RestSourceTask.", this);
    }

    private void pushTrace() {
        //Tracer configuration
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
        Configuration.SenderConfiguration senderConfig = Configuration.SenderConfiguration.fromEnv().withAgentHost("localhost");
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true).withSender(senderConfig);
        Configuration config = new Configuration("poc-camel-connector-service").withSampler(samplerConfig).withReporter(reporterConfig);
        JaegerTracer tracer = config.getTracer();

        sayHello("helloToJaeger", tracer);
    }

    private void sayHello(String helloTo, JaegerTracer tracer) {
        Span span = tracer.buildSpan("say-hello").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            span.setTag("hello-to", helloTo);

            String helloStr = formatString(helloTo, tracer);
            printHello(helloStr, tracer);
        } finally {
            span.finish();
        }
    }

    private String formatString(String helloTo, JaegerTracer tracer) {
        Span span = tracer.buildSpan("formatString").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            String helloStr = String.format("Hello, %s!", helloTo);
            span.log(ImmutableMap.of("event", "string-format", "value", helloStr));
            return helloStr;
        } finally {
            span.finish();
        }
    }

    private void printHello(String helloStr, JaegerTracer tracer) {
        Span span = tracer.buildSpan("printHello").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            System.out.println(helloStr);
            span.log(ImmutableMap.of("event", "println"));
        } finally {
            span.finish();
        }
    }
}
