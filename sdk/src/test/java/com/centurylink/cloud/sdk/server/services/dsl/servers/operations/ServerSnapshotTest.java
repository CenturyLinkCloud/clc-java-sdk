package com.centurylink.cloud.sdk.server.services.dsl.servers.operations;

import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class ServerSnapshotTest extends AbstractServerOperationTest implements WireMockMixin {

    ServerMetadata serverMetadata;

    @Test
    @WireMockFileSource("/snapshot/create")
    public void testCreateSnapshotServer() {
        serverService.createSnapshot(server)
            .waitUntilComplete();

        assert getServerMetadata().getDetails().getSnapshots().size() == 1;
    }

    @Test
    @WireMockFileSource("/snapshot/delete")
    public void testDeleteSnapshotServer() {
        serverService.deleteSnapshot(server.asFilter())
            .waitUntilComplete();

        assert getServerMetadata().getDetails().getSnapshots().size() == 0;
    }

    @Test
    @WireMockFileSource("/snapshot/revert")
    public void testRevertToSnapshotServer() {
        serverService.revertToSnapshot(server.asFilter())
            .waitUntilComplete();

        assert getServerMetadata().getDetails().getSnapshots().size() == 1;
    }

    private ServerMetadata getServerMetadata() {
        return serverService.findByRef(server);
    }
}
