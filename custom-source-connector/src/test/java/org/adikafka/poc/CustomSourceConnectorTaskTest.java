package org.adikafka.poc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomSourceConnectorTaskTest {

    private CustomSourceConnectorTask customSourceConnectorTask;

    @BeforeEach
    public void openMocks() {
        this.customSourceConnectorTask= new CustomSourceConnectorTask();
    }

    @Test
    public void versionTest() {
        assertEquals(customSourceConnectorTask.version(), CustomSourceConnector.VERSION);
    }
}
