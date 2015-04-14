package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.google.inject.Inject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;


@Test(groups = {INTEGRATION, LONG_RUNNING})
public class ServerPowerOperationsServiceTest extends AbstractServersSdkTest {

    private ServerMetadata server;

    @Inject
    ServerService serverService;

    @AfterClass
    public void tearDown() {
        cleanUpCreatedResources(serverService, server.asRefById());
    }

    private ServerMetadata loadActualMetadata(ServerMetadata server) {
        return serverService.findByRef(server.asRefById());
    }

    public void testPowerOff() {
        server = createDefaultServerWithName(serverService, "pwrtst");

        serverService
            .powerOff(server.asRefById())
            .waitUntilComplete();

        ServerMetadata resultServer = loadActualMetadata(this.server);
        assertNotNull(resultServer);
        assertNotNull(resultServer.getDetails());
        assertEquals(resultServer.getDetails().getPowerState(), "stopped");
    }

    @Test
    public void testPowerOn() {
        testPowerOff();

        serverService
            .powerOn(server.asRefById())
            .waitUntilComplete();

        ServerMetadata server = loadActualMetadata(this.server);
        assertNotNull(server);
        assertNotNull(server.getDetails());
        assertNotNull(server.getDetails().getPowerState(), "started");
    }
}
