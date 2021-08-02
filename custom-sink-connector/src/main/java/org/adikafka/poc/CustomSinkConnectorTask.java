package org.adikafka.poc;

import okhttp3.*;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class CustomSinkConnectorTask extends SinkTask {

    private static final Logger log = LoggerFactory.getLogger(CustomSinkConnectorTask.class);
    public CustomSinkConnectorConfig config;

    public void start(Map<String, String> props) {
        config = new CustomSinkConnectorConfig(props);
    }

    public void stop() {
        log.info("{} Stopping RestSinkTask.", this);
    }

    public String version() {
        return CustomSinkConnector.VERSION;
    }

    public void put(Collection<SinkRecord> collection) {
        log.info("+++ PUT VOID STARTS. COLLECTION SIZE: {}", collection.size());
        for (SinkRecord sinkRecord : collection) {
            log.info("+++ COLLECTION PROCESSING STARTED");
            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(
                        sinkRecord.value().toString(), MediaType.parse("application/json"));

                Request request = new Request.Builder()
                        .url("https://enj9dtgx4z1d62y.m.pipedream.net")
                        .post(body)
                        .build();

                log.info("+++ EXECUTE REQUEST");

                Response response = client.newCall(request).execute();
                log.info("+++ RESPONSE CODE: {}", response.code());
                if (response.body() != null) {
                    log.info("+++ RESPONSE BODY: {}", response.body().toString());
                }

                writeToKafka(sinkRecord.key().toString(), response.code());
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToKafka(String key, int code) throws ExecutionException, InterruptedException {
        String server = "localhost:9092";
        String topicName = "output";
        String message = "{\"key\": \"" + key +"\"; \"code:\"" + code + "\";}";

        final Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                server);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());

        final Producer<Long, String> producer =
                new KafkaProducer<>(props);

        RecordMetadata recordMetadata = (RecordMetadata) producer.send(new ProducerRecord(topicName, message)).get();
        if (recordMetadata.hasOffset())
            System.out.println("Message sent successfully");

        producer.close();
    }

}
