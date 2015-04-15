package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.Server;
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

    private Details loadServerDetails(ServerMetadata server) {
        ServerMetadata metadata = serverService.findByRef(server.asRefById());

        assertNotNull(metadata);
        assertNotNull(metadata.getDetails());

        return metadata.getDetails();
    }

    private void assertThat(ServerMetadata server, String status) {
        assert loadServerDetails(server).getPowerState().equals(status);
    }

    private void assertThatMaintenanceFlagIs(ServerMetadata server, Boolean expectedResult) throws Exception {
        Thread.sleep(3000L);
        assert loadServerDetails(server).getInMaintenanceMode().equals(expectedResult);
    }

    public void testPowerOff() {
        server = createDefaultServerWithName(serverService, "pwrtst");

        serverService
            .powerOff(server.asRefById())
            .waitUntilComplete();

        assertThat(server, "stopped");
    }

    public void testPowerOn() {
        testPowerOff();

        serverService
            .powerOn(server.asRefById())
            .waitUntilComplete();

        assertThat(server, "started");
    }

    public void testStartMaintenance() throws Exception {
        testPowerOn();

        serverService
            .startMaintenance(server.asRefById())
            .waitUntilComplete();

        assertThatMaintenanceFlagIs(server, true);
    }

    @Test
    public void testStopMaintenance() throws Exception {
        testStartMaintenance();

        serverService
            .stopMaintenance(server.asRefById())
            .waitUntilComplete();

        assertThatMaintenanceFlagIs(server, false);
    }

}
