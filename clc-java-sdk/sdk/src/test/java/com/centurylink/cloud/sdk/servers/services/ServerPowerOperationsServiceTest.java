package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
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

    private void assertThatPowerStateHasStatus(ServerRef server, String status) {
        assert loadServerDetails(server).getPowerState().equals(status);
    }

    private void assertThatMaintenanceFlagIs(ServerRef server, Boolean expectedResult) throws Exception {
        assert loadServerDetails(server).getInMaintenanceMode().equals(expectedResult);
    }

    private void powerOnServer() {
        serverService
            .powerOn(server)
            .waitUntilComplete();
    }

    private void powerOffServer() {
        serverService
            .powerOff(server)
            .waitUntilComplete();
    }

    private void pauseServer() {
        serverService
            .pause(server)
            .waitUntilComplete();
    }

    private void shutDownServer() {
        serverService
            .shutDown(server)
            .waitUntilComplete();
    }

    private void stopServerMaintenance() {
        serverService
            .stopMaintenance(server)
            .waitUntilComplete();
    }

    private void startServerMaintenance() {
        serverService
            .startMaintenance(server)
            .waitUntilComplete();
    }

    public void testPowerOff() {
        testShutDown();
        powerOffServer();

        assertThatPowerStateHasStatus(server, "stopped");
    }

    public void testPause() {
        pauseServer();

        assertThatPowerStateHasStatus(server, "paused");
        powerOnServer();
    }

    public void testShutDown() {
        testPause();
        shutDownServer();

        assertThatPowerStateHasStatus(server, "stopped");
        powerOnServer();
    }

    public void testPowerOn() {
        testPowerOff();
        powerOnServer();

        assertThatPowerStateHasStatus(server, "started");
    }

    public void testStartMaintenance() throws Exception {
        testPowerOn();
        startServerMaintenance();

        assertThatMaintenanceFlagIs(server, true);
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testStopMaintenance() throws Exception {
        server = SingleServerFixture.server();

        testStartMaintenance();
        stopServerMaintenance();

        assertThatMaintenanceFlagIs(server, false);
    }
}
