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
public class ServerPowerOffTest extends AbstractServerOperationTest implements WireMockMixin {

    @Test
    @WireMockFileSource("/poweroff")
    public void testOffServer() {
        serverService.powerOff(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerPowerStateHasStatus(serverMetadata, STOP);
    }
}
