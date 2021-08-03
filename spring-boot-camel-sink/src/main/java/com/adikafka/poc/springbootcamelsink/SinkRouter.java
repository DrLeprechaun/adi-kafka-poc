package com.adikafka.poc.springbootcamelsink;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SinkRouter extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(SinkRouter.class);

    @Override
    public void configure() throws Exception {

        from("kafka:{{consumer.topic}}?brokers={{kafka.host}}:{{kafka.port}}")
                .routeId("fromKafka")
                .log("Message from Kafka: ${body}")
                .to("{{sink.endpoint}}");
    }
}
