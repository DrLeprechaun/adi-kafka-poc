package org.adikafka.poc;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringConverterTest {

    @InjectMocks
    private StringConverter stringConverter;

    @BeforeEach
    public void openMocks() {
        MockitoAnnotations.openMocks(this);
        this.stringConverter = new StringConverter();
    }

    @Test
    public void configTest() {
        ConfigDef configDef = stringConverter.config();
        assertNotNull(configDef.configKeys().get("converter.type"));
        assertNotNull(configDef.configKeys().get("converter.encoding"));
    }

    @Test
    public void fromConnectDataTest() {
        byte[] result = stringConverter.fromConnectData("topicName", SchemaBuilder.STRING_SCHEMA, "message");
        assertTrue(result.length > 0);
    }

    @Test
    public void toConnectDataTest() {
        SchemaAndValue schemaAndValue = stringConverter.toConnectData("topicName", "someData".getBytes(StandardCharsets.UTF_8));
        assertNotNull(schemaAndValue);
    }

    @Test
    public void fromConnectHeaderTest() {
        byte[] result = stringConverter.fromConnectHeader("topicName", "headerKey", SchemaBuilder.STRING_SCHEMA, "message");
        assertTrue(result.length > 0);
    }

    @Test
    public void toConnectHeaderTest() {
        SchemaAndValue schemaAndValue = stringConverter.toConnectHeader("topicName", "headerKey", "someData".getBytes(StandardCharsets.UTF_8));
        assertNotNull(schemaAndValue);
    }
}
