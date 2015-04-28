package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.IdDataCenterRef;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.fixtures.ServerStubFixture;

import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;


public class ServerOperationsByGroupsTest extends AbstractServerOperationsStubTest {

    @Inject @Mock
    ServerClient serverClient;

    @Inject @Mock
    QueueClient queueClient;

    @Inject @Mock
    DataCentersClient dataCentersClient;

    @Override
    protected void powerOnServer() {
        serverService
            .powerOn(group)
            .waitUntilComplete();
    }

    @Override
    protected void powerOffServer() {
        serverService
            .powerOff(group)
            .waitUntilComplete();
    }

    @Override
    protected void pauseServer() {
        serverService
            .pause(group)
            .waitUntilComplete();
    }

    @Override
    protected void shutDownServer() {
        serverService
            .shutDown(group)
            .waitUntilComplete();
    }

    @Override
    protected void stopServerMaintenance() {
        serverService
            .stopMaintenance(group)
            .waitUntilComplete();
    }

    @Override
    protected void startServerMaintenance() {
        serverService
            .startMaintenance(group)
            .waitUntilComplete();
    }

    @Override
    protected void archiveServer() {
        serverService
            .archive(group)
            .waitUntilComplete();
    }

    @Override
    protected void createServerSnapshot() {
        serverService
            .createSnapshot(1, group)
            .waitUntilComplete();
    }

    @Override
    protected void restoreServer(GroupRef group, ServerRef server) {
        serverService
            .restore(server, group)
            .waitUntilComplete();
    }

    @Override
    protected void resetServer() {
        serverService
            .reset(group)
            .waitUntilComplete();
    }

    @Override
    protected void rebootServer() {
        serverService
            .reboot(group)
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

        group = new IdGroupRef(
                new IdDataCenterRef(fixture.getDataCenterMetadata().getId()),
                serverMetadata1.getGroupId()
        );

        testArchive();
        fixture.activateServers();
    }
}
