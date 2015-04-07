package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.Response;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.TestServerSupport.anyServerConfig;

/**
 * @author ilya.drabenia
 */
@Test(groups = "LongRunning")
public class CreateServerAsyncTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    Response<ServerMetadata> createServerResponse;

    @Test
    public void testCreateServerAsync() throws Exception {
        ListenableFuture<Response<ServerMetadata>> future =
                serverService.createAsync(anyServerConfig().name("CSAC"));

        createServerResponse = future.get();

        assert createServerResponse.getResult().getId() != null;
    }

    @AfterMethod
    public void deleteTestServer() {
        createServerResponse
                .waitUntilComplete();

        serverService
                .delete(createServerResponse.getResult().asRefById())
                .waitUntilComplete();
    }

}
