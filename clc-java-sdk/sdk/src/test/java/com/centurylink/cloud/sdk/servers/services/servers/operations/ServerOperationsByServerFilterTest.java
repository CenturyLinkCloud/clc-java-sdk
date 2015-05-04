package com.centurylink.cloud.sdk.servers.services.servers.operations;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.fixtures.ServerStubFixture;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

public class ServerOperationsByServerFilterTest extends AbstractServerOperationsStubTest {

    @Inject @Mock
    ServerClient serverClient;

    @Inject @Mock
    QueueClient queueClient;

    @Inject @Mock
    DataCentersClient dataCentersClient;

    @Override
    protected void powerOnServer() {
        serverService
            .powerOn(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void powerOffServer() {
        serverService
            .powerOff(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void pauseServer() {
        serverService
            .pause(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void shutDownServer() {
        serverService
            .shutDown(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void stopServerMaintenance() {
        serverService
            .stopMaintenance(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void startServerMaintenance() {
        serverService
            .startMaintenance(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void archiveServer() {
        serverService
            .archive(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void createServerSnapshot() {
        serverService
            .createSnapshot(1, serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void restoreServer(Group group, Server server) {
        serverService
            .restore(server, group)
            .waitUntilComplete();
    }

    @Override
    protected void resetServer() {
        serverService
            .reset(serverFilter)
            .waitUntilComplete();
    }

    @Override
    protected void rebootServer() {
        serverService
            .reboot(serverFilter)
            .waitUntilComplete();
    }

    @Override
    @Test(groups = {INTEGRATION})
    public void runChainTests() {
        ServerStubFixture fixture = new ServerStubFixture(serverClient, queueClient, dataCentersClient);

        ServerMetadata serverMetadata1 = fixture.getServerMetadata();
        ServerMetadata serverMetadata2 = fixture.getAnotherServerMetadata();

        server1 = serverMetadata1.asRefById();
        server2 = serverMetadata2.asRefById();
        serverFilter = fixture.getServerFilterById();

        testArchive();
        fixture.activateServers();
    }
}
