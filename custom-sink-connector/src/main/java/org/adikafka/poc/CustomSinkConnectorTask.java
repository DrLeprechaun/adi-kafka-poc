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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
