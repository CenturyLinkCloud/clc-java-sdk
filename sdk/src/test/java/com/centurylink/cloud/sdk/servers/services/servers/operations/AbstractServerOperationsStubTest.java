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
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.google.inject.Inject;

public abstract class AbstractServerOperationsStubTest extends AbstractServersSdkTest {

    protected Server server1;
    protected Server server2;

    protected ServerFilter serverFilter;
    protected GroupFilter groupFilter;

    protected Group group;

    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    protected ServerMetadata loadServerMetadata(Server server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    protected Details loadServerDetails(Server server) {
        ServerMetadata metadata = loadServerMetadata(server);
        assertNotNull(metadata.getDetails());

        return metadata.getDetails();
    }

    protected void assertThatServerHasStatus(Server server, String status) {
        assertEquals(loadServerMetadata(server).getStatus(), status);
    }

    protected void assertThatServerPowerStateHasStatus(Server server, String status) {
        assertEquals(loadServerDetails(server).getPowerState(), status);
    }

    protected void assertThatMaintenanceFlagIs(Server server, Boolean expectedResult) {
        assertEquals(loadServerDetails(server).getInMaintenanceMode(), expectedResult);
    }

    protected abstract void powerOnServer();

    protected abstract void powerOffServer();

    protected abstract void pauseServer();

    protected abstract void shutDownServer();

    protected abstract void stopServerMaintenance();

    protected abstract void startServerMaintenance();

    protected abstract void archiveServer();

    protected abstract void createServerSnapshot();

    protected abstract void restoreServer(Group group, Server server);

    protected abstract void resetServer();

    protected abstract void rebootServer();

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


    public void testArchive() {
        testReset();

        assertThatServerHasStatus(server1, "active");
        assertThatServerHasStatus(server2, "active");

        archiveServer();

        assertThatServerHasStatus(server1, "archived");
        assertThatServerHasStatus(server2, "archived");
    }

    public abstract void runChainTests();
}
