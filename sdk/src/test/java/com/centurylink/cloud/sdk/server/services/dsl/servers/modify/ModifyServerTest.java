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

package com.centurylink.cloud.sdk.server.services.dsl.servers.modify;

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.Disk;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ModifyServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
public class ModifyServerTest extends AbstractServersSdkTest implements WireMockMixin {

    private static final String initialDescription = "";
    private static final Integer initialCpu = 1;
    private static final Integer initialRam = 2;

    private static final String modifiedDescription = "Description for tests";
    private static final Integer modifiedCpu = 2;
    private static final Integer modifiedRam = 4;

    private static final Integer disk1ModifiedSize = 3;
    private static final Integer disk2ModifiedSize = 5;
    private static final Integer disk3ModifiedSize = 19;

    @Inject
    ServerService serverService;

    Server server;

    ServerMetadata serverMetadata;

    private void createServer() {
        server = serverService.create(
            TestServerSupport
                .anyServerConfig()
                .name("md-srv")
                .description(initialDescription)
                .machine(
                    new Machine()
                        .cpuCount(initialCpu)
                        .ram(initialRam)
                )
        )
        .waitUntilComplete()
        .getResult();

        fetchLastServerMetadata();
        assertNotNull(server);
    }

    private void deleteServer() {
        serverService.delete(server);
    }

    private void sendModifyRequest() {
        Machine machineConfig = new Machine()
            .cpuCount(modifiedCpu)
                .ram(modifiedRam)
                .disk(
                    new DiskConfig().diskId("0:0").size(disk1ModifiedSize)
                )
                .disk(
                    new DiskConfig().diskId("0:1").size(disk2ModifiedSize)
                )
                .disk(
                    new DiskConfig().diskId("0:2").size(disk3ModifiedSize)
                );

        ModifyServerConfig modifyServerConfig = new ModifyServerConfig()
            .machineConfig(machineConfig)
            .description(modifiedDescription);

        serverService.modify(server, modifyServerConfig).waitUntilComplete();
    }

    private void fetchLastServerMetadata() {
        serverMetadata = serverService.findByRef(server);
    }

    private void checkInitialServerState() {
        assertEquals(serverMetadata.getDescription(), initialDescription);
        Assert.assertEquals(serverMetadata.getDetails().getCpu(), initialCpu);
        Assert.assertEquals(serverMetadata.getDetails().getMemoryMB(), Integer.valueOf(initialRam * 1024));
    }

    private void checkServerStateAfterUpdate() {
        assertEquals(serverMetadata.getDescription(), modifiedDescription);
        Assert.assertEquals(serverMetadata.getDetails().getCpu(), modifiedCpu);
        Assert.assertEquals(serverMetadata.getDetails().getMemoryMB(), Integer.valueOf(modifiedRam * 1024));

        List<Disk> disks = serverMetadata.getDetails().getDisks();

        assertEquals(disks.size(), 3);
        assertEquals(disks.get(1).getSizeGB(), disk2ModifiedSize);
        assertEquals(disks.get(2).getSizeGB(), disk3ModifiedSize);
    }

    @Test
    @WireMockFileSource("/created")
    public void testCreateServer() throws Exception {
        createServer();
        checkInitialServerState();
    }

    @Test
    @WireMockFileSource("/updated")
    public void testModifyServer() throws Exception {
        sendModifyRequest();
        fetchLastServerMetadata();
        checkServerStateAfterUpdate();

        deleteServer();
    }

}
