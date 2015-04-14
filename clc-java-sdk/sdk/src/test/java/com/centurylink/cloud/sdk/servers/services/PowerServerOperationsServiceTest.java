package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;

import com.google.inject.Inject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = LONG_RUNNING)
public class PowerServerOperationsServiceTest extends AbstractServersSdkTest {

    private ServerMetadata server1;

    @Inject
    ServerService serverService;

    @BeforeMethod
    public void setUp() throws Exception {
        server1 = createDefaultServerWithName(serverService, "ser-1");
    }

    @AfterClass
    public void tearDown() {
        cleanUpCreatedResources(serverService, server1.asRefById());
    }

    @Test
    public void testServerPower() throws Exception {
        List<ServerRef> serverList = new ArrayList<>();
        serverList.add(server1.asRefById());

        serverService.powerOn(serverList).waitUntilComplete().getResult();
        server1 = serverService.findByRef(server1.asRefById());

        assertNotNull(server1);
        assertNotNull(server1.getDetails());
        assertEquals(server1.getDetails().getPowerState(), "started");

        serverService.powerOff(serverList).waitUntilComplete().getResult();

        server1 = serverService.findByRef(server1.asRefById());

        assertNotNull(server1);
        assertNotNull(server1.getDetails());
        assertNotNull(server1.getDetails().getPowerState(), "stopped");
    }
}
