package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.google.inject.Inject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.Arrays.asList;


@Test(groups = {INTEGRATION, LONG_RUNNING})
public class ServerPowerOperationsServiceTest extends AbstractServersSdkTest {

    private ServerMetadata server1;

    @Inject
    ServerService serverService;

    @BeforeMethod
    public void setUp() throws Exception {
        server1 = createDefaultServerWithName(serverService, "ser-1");
    }

    @AfterClass
    public void tearDown() {
        cleanUpCreatedResources(serverService, server1.asRefById());
    }

    public void testStartServerMaintenance() {
        serverService
            .powerOn(server1.asRefById())
            .waitUntilComplete();

        ServerMetadata server = serverService.findByRef(server1.asRefById());
        assertNotNull(server);
        assertNotNull(server.getDetails());
        assertEquals(server.getDetails().getPowerState(), "started");
    }

    @Test
    public void testStopServerMaintenance() {
        testStartServerMaintenance();

        serverService
            .powerOff(server1.asRefById())
            .waitUntilComplete();

        ServerMetadata server = serverService.findByRef(server1.asRefById());
        assertNotNull(server);
        assertNotNull(server.getDetails());
        assertNotNull(server.getDetails().getPowerState(), "stopped");
    }
}
