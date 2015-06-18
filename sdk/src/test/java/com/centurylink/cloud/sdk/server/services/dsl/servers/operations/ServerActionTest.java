package com.centurylink.cloud.sdk.server.services.dsl.servers.operations;

import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = RECORDED)
public class ServerActionTest extends AbstractServerOperationTest implements WireMockMixin {

    Group group = Group.refById("68c30c5898584105902306f7f610b31b");

    @Test
    @WireMockFileSource("/archive")
    public void testArchiveServer() {
        serverService.archive(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerHasStatus(serverMetadata, ARCHIVED);
    }

    @Test
    @WireMockFileSource("/reboot")
    public void testRebootServer() {
        serverService.reboot(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerHasStatus(serverMetadata, ACTIVE);
    }

    @Test
    @WireMockFileSource("/restore")
    public void testRestoreServer() {
        serverService.restore(server, group)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assertThatServerHasStatus(serverMetadata, ACTIVE);
    }
}

