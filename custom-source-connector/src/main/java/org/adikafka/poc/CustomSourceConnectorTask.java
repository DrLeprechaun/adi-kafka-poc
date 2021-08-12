package org.adikafka.poc;

import com.google.common.collect.ImmutableMap;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.tag.Tags;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.adikafka.poc.holmes.logger.HolmesMessage;
import org.adikafka.poc.holmes.model.AuditType;
import org.adikafka.poc.holmes.model.Level;
import org.adikafka.poc.holmes.model.impl.*;
import org.adikafka.poc.holmes.util.LoggingUtils;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.adikafka.poc.holmes.config.LoggingConfigProvider.getLoggingConfig;
import static org.adikafka.poc.holmes.constant.LoggingConstants.LOGGING_KAFKA_VERSION;

public class CustomSourceConnectorTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(CustomSourceConnectorTask.class);
    public CustomSourceConnectorConfig config;
    private JaegerTracer tracer;

    @Override
    public String version() {
        return CustomSourceConnector.VERSION;
    }

    @Override
    public void start(Map<String, String> props) {
        config = new CustomSourceConnectorConfig(props);
        //Tracer configuration
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
        Configuration.SenderConfiguration senderConfig = Configuration.SenderConfiguration.fromEnv().withAgentHost("host.docker.internal");
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true).withSender(senderConfig);
        Configuration config = new Configuration("poc-source-connector-service").withSampler(samplerConfig).withReporter(reporterConfig);
        this.tracer = config.getTracer();
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
            records.add(buildRecord(response));
            pollRequestSpan(response.isSuccessful(), response.code());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logHolmes();
        }
        return records;
    }

    @Override
    public void stop() {
        log.info("{} Stopping RestSourceTask.", this);
    }

    private SourceRecord buildRecord(Response response) throws IOException {
        Schema schema = SchemaBuilder.struct()
                .name("ItemValue")
                .parameters(Collections.emptyMap())
                .field("value", Schema.STRING_SCHEMA)
                .build();
        Struct struct = new Struct(schema).put("value", Objects.requireNonNull(response.body()).string());
        return new SourceRecord(Collections.emptyMap(), Collections.emptyMap(), "output", schema, struct);
    }

    private void logHolmes() {
        //Holmes log
        HolmesLog holmesLog = HolmesLog.newBuilder()
                .message("Kafka Source Connector: Sample Holmes log message")
                .timestampNow()
                .labels(Label.DEV)
                .service(Service.newBuilder()
                        .ephemeralId(getLoggingConfig().getPropertyWithDefaultUnknown("ephemeralId"))
                        .version(getLoggingConfig().getPropertyWithDefaultUnknown(LOGGING_KAFKA_VERSION))
                        .build()
                )
                .log(Log.newBuilder()
                        .level(Level.INFO)
                        .logMessage(AuditMessage.newBuilder()
                                .timestampNow()
                                .corrID("corrID-value")
                                .bussObjID("bussObjID-value")
                                .logAttribute("LOG_ATTRIBUTE", "log attribute value")
                                .additionalMsg("additional message")
                                .auditType(AuditType.END).build()
                        )
                        .initializeMessageContext(LoggingUtils.INITIALIZE_MESSAGE_CONTEXT_OBJECT).build()
                ).build();
        log.info("+++ HOLMES message: " + new HolmesMessage(holmesLog).getFormattedMessage());
    }

    private void pollRequestSpan(boolean isSucceded, int code) {
        Span span = tracer.buildSpan("poll").start();
        try(Scope scope = tracer.scopeManager().activate(span)) {
            responseSpan(isSucceded, code);
            toKafkaSpan();
        } finally {
            span.finish();
        }
    }

    private void responseSpan(boolean isSucceded, int code) {
        Span span = tracer.buildSpan("response").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            span.log(ImmutableMap.of("responseCode", String.valueOf(code)));
            if (!isSucceded) {
                span.setTag(Tags.ERROR, true);
            }
        } finally {
            span.finish();
        }
    }

    private void toKafkaSpan() {
        Span span = tracer.buildSpan("toKafka").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            span.setTag("step", "toKafkaConnect");
            span.log("The response from endpoint was passed downstream to Kafka Connect ");
        } finally {
            span.finish();
        }
    }
}
