package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;


public class ServerPowerOperationsServiceTest extends AbstractServersSdkTest {

    private ServerRef server;

    @Inject
    ServerService serverService;

    private Details loadServerDetails(ServerRef server) {
        ServerMetadata metadata = serverService.findByRef(server);

        assertNotNull(metadata);
        assertNotNull(metadata.getDetails());

        return metadata.getDetails();
    }

    private void assertThat(ServerRef server, String status) {
        assert loadServerDetails(server).getPowerState().equals(status);
    }

    private void assertThatMaintenanceFlagIs(ServerRef server, Boolean expectedResult) throws Exception {
        assert loadServerDetails(server).getInMaintenanceMode().equals(expectedResult);
    }

    public void testPowerOff() {
        server = SingleServerFixture.server();

        serverService
            .powerOff(server)
            .waitUntilComplete();

        assertThat(server, "stopped");
    }

    public void testPowerOn() {
        testPowerOff();

        serverService
            .powerOn(server)
            .waitUntilComplete();

        assertThat(server, "started");
    }

    public void testStartMaintenance() throws Exception {
        testPowerOn();

        serverService
            .startMaintenance(server)
            .waitUntilComplete();

        assertThatMaintenanceFlagIs(server, true);
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testStopMaintenance() throws Exception {
        testStartMaintenance();

        serverService
            .stopMaintenance(server)
            .waitUntilComplete();

        assertThatMaintenanceFlagIs(server, false);
    }

}
