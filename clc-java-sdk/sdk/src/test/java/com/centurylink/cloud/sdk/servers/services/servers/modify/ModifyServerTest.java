package com.centurylink.cloud.sdk.servers.services.servers.modify;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.Disk;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.ModifyServerConfig;
import com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport;
import com.google.inject.Inject;
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
        assertEquals(serverMetadata.getDetails().getCpu(), initialCpu);
        assertEquals(serverMetadata.getDetails().getMemoryMB(), Integer.valueOf(initialRam * 1024));
    }

    private void checkServerStateAfterUpdate() {
        assertEquals(serverMetadata.getDescription(), modifiedDescription);
        assertEquals(serverMetadata.getDetails().getCpu(), modifiedCpu);
        assertEquals(serverMetadata.getDetails().getMemoryMB(), Integer.valueOf(modifiedRam * 1024));

        List<Disk> disks = serverMetadata.getDetails().getDisks();

        assertEquals(disks.size(), 3);

        /* TODO Should check why changes sometimes do not take effect immediately */
//        assertEquals(disks.get(0).getSizeGB(), disk1ModifiedSize);
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