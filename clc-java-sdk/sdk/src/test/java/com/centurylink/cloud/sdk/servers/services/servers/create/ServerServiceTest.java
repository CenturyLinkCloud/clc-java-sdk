package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef;
import com.google.inject.Inject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

/**
 * @author ilya.drabenia
 */
@Test
public class ServerServiceTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Test(groups = {INTEGRATION}, expectedExceptions = ReferenceNotResolvedException.class)
    public void testGetServer() {
        serverService.findByRef(Server.refById("randomString"));
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testAddPublicIp() {
        ServerByIdRef serverRef = Server.refById("DE1ALTRATL101");
        Assert.assertNotNull(serverRef);
        OperationFuture<Server> future = serverService.addPublicIp(serverRef, new CreatePublicIpConfig().openPorts(22));
        Server serverWithIp = future.waitUntilComplete().getResult();
        Assert.assertNotNull(serverWithIp);
    }
}
