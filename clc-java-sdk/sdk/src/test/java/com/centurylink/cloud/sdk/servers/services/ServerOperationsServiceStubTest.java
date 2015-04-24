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

    private ServerRef server;

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

    private void restoreServer(GroupRef group) {
        serverService
            .restore(server, group)
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

    public void testStartMaintenance() {
        testPowerOn();
        startServerMaintenance();

        assertThatMaintenanceFlagIs(server, true);
    }

    public void testStopMaintenance() {
        testStartMaintenance();
        stopServerMaintenance();

        assertThatMaintenanceFlagIs(server, false);
    }

    public void testCreateSnapshot() {
        testStopMaintenance();

        Details serverDetails = loadServerDetails(server);
        assertEquals(serverDetails.getSnapshots().size(), 0);

        createServerSnapshot();

        serverDetails = loadServerDetails(server);
        assertEquals(serverDetails.getSnapshots().size(), 1);
    }

    public void testArchive() {
        testCreateSnapshot();

        assertThatServerHasStatus(server, "active");
        archiveServer();
        assertThatServerHasStatus(server, "archived");
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testRestore() throws Exception {
        ServerStubFixture fixture = new ServerStubFixture(serverClient, queueClient);

        ServerMetadata serverMetadata = fixture.getServerMetadata();
        server = serverMetadata.asRefById();
        serverFilter = new ServerFilter().idIn(serverMetadata.getId());

        String groupId = loadServerMetadata(server).getGroupId();
        GroupRef group = new IdGroupRef(DataCenter.refByName("FranKfUrt"), groupId);

        testArchive();

//        restoreServer(group);
//        assertThatServerHasStatus(server, "active");
    }
}
