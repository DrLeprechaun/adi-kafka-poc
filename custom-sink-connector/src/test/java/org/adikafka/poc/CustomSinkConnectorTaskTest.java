package org.adikafka.poc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomSinkConnectorTaskTest {

    private CustomSinkConnectorTask customSinkConnectorTask;

    @BeforeEach
    public void openMocks() {
        this.customSinkConnectorTask = new CustomSinkConnectorTask();
    }

    @Test
    public void versionTest() {
        assertEquals(customSinkConnectorTask.version(), CustomSinkConnector.VERSION);
    }

    @Test
    public void putTest() {
        //TODO: implement after method refactoring
    }
}
