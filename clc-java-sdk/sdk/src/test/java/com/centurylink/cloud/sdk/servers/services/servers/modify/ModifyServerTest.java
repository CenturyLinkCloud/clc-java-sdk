package com.centurylink.cloud.sdk.servers.services.servers.modify;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.ModifyServerConfig;
import com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class ModifyServerTest extends AbstractServersSdkTest {

    private final static String oldDescription = "";
    private final static Integer oldCpu = 1;
    private final static Integer oldRam = 2;

    private final static String newDescription = "Description for tests";
    private final static Integer newCpu = 2;
    private final static Integer newRam = 4;

    @Inject
    ServerService serverService;

    ServerMetadata serverMetadata;

    @BeforeMethod
    public void createServer() {
        serverMetadata = serverService.create(
            TestServerSupport
                .anyServerConfig()
                .name("md-srv")
                .description(oldDescription)
                .machine(
                    new Machine()
                        .cpuCount(oldCpu)
                        .ram(oldRam)
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
                .cpuCount(newCpu)
                .ram(newRam);

        ModifyServerConfig modifyServerConfig = new ModifyServerConfig()
            .machineConfig(machineConfig)
            .description(newDescription);

        serverService.modify(serverMetadata.asRefById(), modifyServerConfig).waitUntilComplete();
    }

    private void fetchLastServerMetadata() {
        serverMetadata = serverService.findByRef(serverMetadata.asRefById());
    }

    private void checkInitialServerState() {
        assertEquals(serverMetadata.getDescription(), oldDescription);
        assertEquals(serverMetadata.getDetails().getCpu(), oldCpu);
        assertEquals(serverMetadata.getDetails().getMemoryMB(), Integer.valueOf(oldRam * 1024));
    }

    private void checkServerStateAfterUpdate() {
        assertEquals(serverMetadata.getDescription(), newDescription);
        assertEquals(serverMetadata.getDetails().getCpu(), newCpu);
        assertEquals(serverMetadata.getDetails().getMemoryMB(), Integer.valueOf(newRam * 1024));
    }

    @Test
    public void testModifyServer() throws Exception {
        checkInitialServerState();

        sendModifyRequest();
        fetchLastServerMetadata();

        checkServerStateAfterUpdate();
    }

}
