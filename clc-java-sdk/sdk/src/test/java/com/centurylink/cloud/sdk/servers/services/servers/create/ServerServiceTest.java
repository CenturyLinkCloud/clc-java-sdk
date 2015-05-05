package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

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
}
