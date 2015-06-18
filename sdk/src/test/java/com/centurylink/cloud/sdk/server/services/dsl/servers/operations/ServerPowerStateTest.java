package com.centurylink.cloud.sdk.server.services.dsl.servers.operations;

import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = RECORDED)
public class ServerPowerStateTest extends AbstractServerOperationTest implements WireMockMixin {

    @Test
    @WireMockFileSource("/start")
    public void testStartServer() {
        serverService.powerOn(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerPowerStateHasStatus(serverMetadata, START);
    }

    @Test
    @WireMockFileSource("/stop")
    public void testStopServer() {
        serverService.shutDown(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerPowerStateHasStatus(serverMetadata, STOP);
    }

    @Test
    @WireMockFileSource("/pause")
    public void testPauseServer() {
        serverService.pause(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerPowerStateHasStatus(serverMetadata, PAUSE);
    }

    @Test
    @WireMockFileSource("/poweron")
    public void testOnServer() {
        serverService.powerOn(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerPowerStateHasStatus(serverMetadata, START);
    }

    @Test
    @WireMockFileSource("/poweroff")
    public void testOffServer() {
        serverService.powerOff(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerPowerStateHasStatus(serverMetadata, STOP);
    }
}
