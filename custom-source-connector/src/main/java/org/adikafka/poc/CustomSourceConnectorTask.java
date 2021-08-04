package org.adikafka.poc;

import com.google.common.collect.ImmutableMap;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.util.List;
import java.util.Map;

public class CustomSourceConnectorTask extends SourceTask {
    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> map) {

    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        return null;
    }

    @Override
    public void stop() {

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
