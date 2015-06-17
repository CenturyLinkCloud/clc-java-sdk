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

import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.Disk;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ModifyServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.google.inject.Inject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class ModifyServerTest extends AbstractServersSdkTest {

    private final static String initialDescription = "";
    private final static Integer initialCpu = 1;
    private final static Integer initialRam = 2;

    private final static String modifiedDescription = "Description for tests";
    private final static Integer modifiedCpu = 2;
    private final static Integer modifiedRam = 4;

    private final static Integer disk1ModifiedSize = 3;
    private final static Integer disk2ModifiedSize = 5;
    private final static Integer disk3ModifiedSize = 19;

    @Inject
    ServerService serverService;

    ServerMetadata serverMetadata;

    @BeforeMethod
    public void createServer() {
        serverMetadata = serverService.create(
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
        assertNotNull(serverMetadata);
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(serverMetadata.asRefById());
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

        serverService.modify(serverMetadata.asRefById(), modifyServerConfig).waitUntilComplete();
    }

    private void fetchLastServerMetadata() {
        serverMetadata = serverService.findByRef(serverMetadata.asRefById());
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
    public void testModifyServer() throws Exception {
        checkInitialServerState();

        sendModifyRequest();
        fetchLastServerMetadata();

        checkServerStateAfterUpdate();
    }

}
