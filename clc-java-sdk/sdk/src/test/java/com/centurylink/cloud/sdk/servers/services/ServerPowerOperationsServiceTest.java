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

    private ServerMetadata loadServerMetadata(ServerRef server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    private Details loadServerDetails(ServerRef server) {
        ServerMetadata metadata = loadServerMetadata(server);
        assertNotNull(metadata.getDetails());

        return metadata.getDetails();
    }

    private void assertThatServerHasStatus(ServerRef server, String status) {
        assertEquals(loadServerMetadata(server).getStatus(), status);
    }

    private void assertThatServerPowerStateHasStatus(ServerRef server, String status) {
        assertEquals(loadServerDetails(server).getPowerState(), status);
    }

    private void assertThatMaintenanceFlagIs(ServerRef server, Boolean expectedResult) throws Exception {
        assertEquals(loadServerDetails(server).getInMaintenanceMode(), expectedResult);
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

    private void archiveServer() {
        serverService
            .archive(server)
            .waitUntilComplete();
    }

    public void testPowerOff() {
        testShutDown();
        powerOffServer();

        assertThatServerPowerStateHasStatus(server, "stopped");
    }

    public void testPause() {
        pauseServer();

        assertThatServerPowerStateHasStatus(server, "paused");
        powerOnServer();
    }

    public void testShutDown() {
        testPause();
        shutDownServer();

        assertThatServerPowerStateHasStatus(server, "stopped");
        powerOnServer();
    }

    public void testPowerOn() {
        testPowerOff();
        powerOnServer();

        assertThatServerPowerStateHasStatus(server, "started");
    }

    public void testStartMaintenance() throws Exception {
        testPowerOn();
        startServerMaintenance();

        assertThatMaintenanceFlagIs(server, true);
    }

    public void testStopMaintenance() throws Exception {
        testStartMaintenance();
        stopServerMaintenance();

        assertThatMaintenanceFlagIs(server, false);
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testArchive() throws Exception {
        server = SingleServerFixture.server();

        testStopMaintenance();
        assertThatServerHasStatus(server, "active");

        archiveServer();
        assertThatServerHasStatus(server, "archived");
    }
}
