package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef;
import com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

/**
 * @author ilya.drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class ServerServiceTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Test(expectedExceptions = ReferenceNotResolvedException.class)
    public void testDeleteServers() {
        OperationFuture<ServerMetadata> future1 = serverService.create(TestServerSupport.anyServerConfig());
        OperationFuture<ServerMetadata> future2 = serverService.create(TestServerSupport.anyServerConfig());

        ServerMetadata testServer1 = future1
                .waitUntilComplete()
                .getResult();

        ServerMetadata testServer2 = future2
                .waitUntilComplete()
                .getResult();

        Server ref1 = testServer1.asRefById();
        Server ref2 = testServer2.asRefById();

        serverService.delete(ref1.asFilter().id(ref2.as(ServerByIdRef.class).getId())).waitUntilComplete();

        //catch only 1st call of finding server
        try {
            serverService.findByRef(ref1);
        } catch (ReferenceNotResolvedException e) {
            serverService.findByRef(ref2);
        }

    }

    void cleanUpCreatedResources(Server newServer) {
        serverService
            .delete(newServer);
    }

}
