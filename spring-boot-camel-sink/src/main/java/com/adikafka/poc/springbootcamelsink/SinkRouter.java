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
        /*from("kafka:input?brokers=localhost:9092")
                .routeId("FromKafka")
                .log("${body}");*/

        from("kafka:{{consumer.topic}}?brokers={{kafka.host}}:{{kafka.port}}")
                .routeId("fromKafka")
                .log("${body}");

        // Kafka Consumer
        /*from("kafka:input?brokers=localhost:9092")
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}");*/
    }
}
