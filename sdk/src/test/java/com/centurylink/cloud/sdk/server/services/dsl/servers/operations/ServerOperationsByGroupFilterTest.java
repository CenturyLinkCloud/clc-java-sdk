/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.server.services.dsl.servers.operations;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.QueueClient;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.tests.fixtures.ServerStubFixture;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;


public class ServerOperationsByGroupFilterTest extends AbstractServerOperationsStubTest {

    @Inject @Mock
    ServerClient serverClient;

    @Inject @Mock
    QueueClient queueClient;

    @Inject @Mock
    DataCentersClient dataCentersClient;

    @Override
    protected void powerOnServer() {
        groupService
            .powerOn(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void powerOffServer() {
        groupService
            .powerOff(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void pauseServer() {
        groupService
            .pause(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void shutDownServer() {
        groupService
            .shutDown(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void stopServerMaintenance() {
        groupService
            .stopMaintenance(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void startServerMaintenance() {
        groupService
            .startMaintenance(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void archiveServer() {
        groupService
            .archive(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void createServerSnapshot() {
        groupService
            .createSnapshot(1, groupFilter)
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
        groupService
            .reset(groupFilter)
            .waitUntilComplete();
    }

    @Override
    protected void rebootServer() {
        groupService
            .reboot(groupFilter)
            .waitUntilComplete();
    }

    @Override
    @Test
    public void runChainTests() {
        ServerStubFixture fixture = new ServerStubFixture(serverClient, queueClient, dataCentersClient);

        ServerMetadata serverMetadata1 = fixture.getServerMetadata();
        ServerMetadata serverMetadata2 = fixture.getAnotherServerMetadata();

        server1 = serverMetadata1.asRefById();
        server2 = serverMetadata2.asRefById();
        groupFilter = fixture.getGroupFilterById();

        testArchive();
        fixture.activateServers();
    }
}
