package org.adikafka.poc;

import okhttp3.*;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        for (SinkRecord sinkRecord : collection) {
            // TODO: your implementation goes here
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(
                    sinkRecord.value().toString(), MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(config.getString("https://enj9dtgx4z1d62y.m.pipedream.net"))
                    .post(body)
                    .build();

            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
