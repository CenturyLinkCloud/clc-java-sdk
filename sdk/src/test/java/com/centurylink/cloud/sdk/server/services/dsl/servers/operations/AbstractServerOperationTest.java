package com.centurylink.cloud.sdk.server.services.dsl.servers.operations;

import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.google.inject.Inject;

/**
 * @author Aliaksandr Krasitski
 */
public class AbstractServerOperationTest extends AbstractServersSdkTest {

    static final String PAUSE = "paused";
    static final String STOP = "stopped";
    static final String START = "started";
    static final String ACTIVE = "active";
    static final String ARCHIVED = "archived";

    Server server = Server.refById("ca1altdpwrops01");

    @Inject
    ServerService serverService;

    protected void assertThatServerHasStatus(ServerMetadata serverMetadata, String status) {
        assertEquals(serverMetadata.getStatus(), status);
    }

    protected void assertThatServerPowerStateHasStatus(ServerMetadata serverMetadata, String status) {
        assertEquals(serverMetadata.getDetails().getPowerState(), status);
    }

    protected void assertThatMaintenanceFlagIs(ServerMetadata serverMetadata, Boolean expectedResult) {
        assertEquals(serverMetadata.getDetails().getInMaintenanceMode(), expectedResult);
    }

}
