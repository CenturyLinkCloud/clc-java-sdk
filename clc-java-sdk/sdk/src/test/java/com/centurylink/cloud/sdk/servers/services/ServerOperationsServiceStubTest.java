package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.fixtures.ServerStubFixture;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

public class ServerOperationsServiceStubTest extends AbstractServersSdkTest {

    private ServerRef server1;
    private ServerRef server2;

    private ServerFilter serverFilter;

    @Inject
    ServerService serverService;

    @Mock
    ServerClient serverClient;

    @Mock
    QueueClient queueClient;

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

    private void assertThatMaintenanceFlagIs(ServerRef server, Boolean expectedResult) {
        assertEquals(loadServerDetails(server).getInMaintenanceMode(), expectedResult);
    }

    private void powerOnServer() {
        serverService
            .powerOn(serverFilter)
            .waitUntilComplete();
    }

    private void powerOffServer() {
        serverService
            .powerOff(serverFilter)
            .waitUntilComplete();
    }

    private void pauseServer() {
        serverService
            .pause(serverFilter)
            .waitUntilComplete();
    }

    private void shutDownServer() {
        serverService
            .shutDown(serverFilter)
            .waitUntilComplete();
    }

    private void stopServerMaintenance() {
        serverService
            .stopMaintenance(serverFilter)
            .waitUntilComplete();
    }

    private void startServerMaintenance() {
        serverService
            .startMaintenance(serverFilter)
            .waitUntilComplete();
    }

    private void archiveServer() {
        serverService
            .archive(serverFilter)
            .waitUntilComplete();
    }

    private void createServerSnapshot() {
        serverService
            .createSnapshot(1, serverFilter)
            .waitUntilComplete();
    }

    private void restoreServer(GroupRef group, ServerRef server) {
        serverService
            .restore(server, group)
            .waitUntilComplete();
    }

    private void resetServer() {
        serverService
            .reset(serverFilter)
            .waitUntilComplete();
    }

    private void rebootServer() {
        serverService
            .reboot(serverFilter)
            .waitUntilComplete();
    }

    public void testPowerOff() {
        testShutDown();
        powerOffServer();

        assertThatServerPowerStateHasStatus(server1, "stopped");
        assertThatServerPowerStateHasStatus(server2, "stopped");
    }

    public void testPause() {
        pauseServer();

        assertThatServerPowerStateHasStatus(server1, "paused");
        assertThatServerPowerStateHasStatus(server2, "paused");
        powerOnServer();
    }

    public void testShutDown() {
        testPause();
        shutDownServer();

        assertThatServerPowerStateHasStatus(server1, "stopped");
        assertThatServerPowerStateHasStatus(server2, "stopped");
        powerOnServer();
    }

    public void testPowerOn() {
        testPowerOff();
        powerOnServer();

        assertThatServerPowerStateHasStatus(server1, "started");
        assertThatServerPowerStateHasStatus(server2, "started");
    }

    public void testStartMaintenance() {
        testPowerOn();
        startServerMaintenance();

        assertThatMaintenanceFlagIs(server1, true);
        assertThatMaintenanceFlagIs(server2, true);
    }

    public void testStopMaintenance() {
        testStartMaintenance();
        stopServerMaintenance();

        assertThatMaintenanceFlagIs(server1, false);
        assertThatMaintenanceFlagIs(server2, false);
    }

    public void testCreateSnapshot() {
        testStopMaintenance();

        assertEquals(loadServerDetails(server1).getSnapshots().size(), 0);
        assertEquals(loadServerDetails(server2).getSnapshots().size(), 0);

        createServerSnapshot();

        assertEquals(loadServerDetails(server1).getSnapshots().size(), 1);
        assertEquals(loadServerDetails(server2).getSnapshots().size(), 1);
    }

    public void testRebootServer() {
        testCreateSnapshot();

        rebootServer();

        assertThatServerPowerStateHasStatus(server1, "started");
        assertThatServerPowerStateHasStatus(server2, "started");
    }

    public void testReset() {
        testRebootServer();

        resetServer();

        assertThatServerPowerStateHasStatus(server1, "started");
        assertThatServerPowerStateHasStatus(server2, "started");
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testArchive() {
        ServerStubFixture fixture = new ServerStubFixture(serverClient, queueClient);

        ServerMetadata serverMetadata1 = fixture.getServerMetadata();
        ServerMetadata serverMetadata2 = fixture.getAnotherServerMetadata();

        server1 = serverMetadata1.asRefById();
        server2 = serverMetadata2.asRefById();
        serverFilter = new ServerFilter().idIn(serverMetadata1.getId(), serverMetadata2.getId());

        testReset();

        assertThatServerHasStatus(server1, "active");
        assertThatServerHasStatus(server2, "active");

        archiveServer();

        assertThatServerHasStatus(server1, "archived");
        assertThatServerHasStatus(server2, "archived");
    }
}
