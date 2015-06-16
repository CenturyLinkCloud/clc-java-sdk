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

package com.centurylink.cloud.sdk.servers.services.servers.operations;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

public class ServerOperationsServiceTest extends AbstractServersSdkTest {

    private Server server;

    @Inject
    ServerService serverService;

    private ServerMetadata loadServerMetadata(Server server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    private Details loadServerDetails(Server server) {
        ServerMetadata metadata = loadServerMetadata(server);
        assertNotNull(metadata.getDetails());

        return metadata.getDetails();
    }

    private void assertThatServerHasStatus(Server server, String status) {
        assertEquals(loadServerMetadata(server).getStatus(), status);
    }

    private void assertThatServerPowerStateHasStatus(Server server, String status) {
        assertEquals(loadServerDetails(server).getPowerState(), status);
    }

    private void assertThatMaintenanceFlagIs(Server server, Boolean expectedResult) {
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

    private void createServerSnapshot() {
        serverService
            .createSnapshot(1, server)
            .waitUntilComplete();
    }

    private void restoreServer(Group group) {
        serverService
            .restore(server, group)
            .waitUntilComplete();
    }

    private void revertToSnapshot() {
        serverService
            .revertToSnapshot(server.asFilter())
            .waitUntilComplete();
    }

    private void deleteSnapshot() {
        serverService
            .deleteSnapshot(server.asFilter())
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

    public void testRevertToSnapshot() {
        Details serverDetails = loadServerDetails(server);
        assertEquals(serverDetails.getSnapshots().size(), 1);

        revertToSnapshot();

        deleteSnapshot();

        serverDetails = loadServerDetails(server);
        assertEquals(serverDetails.getSnapshots().size(), 0);
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testRestore() throws Exception {
        server = SingleServerFixture.server();

        String groupId = loadServerMetadata(server).getGroupId();
        Group group = Group.refById(groupId);

        testArchive();
        restoreServer(group);

        testRevertToSnapshot();

        assertThatServerHasStatus(server, "active");
    }

}
