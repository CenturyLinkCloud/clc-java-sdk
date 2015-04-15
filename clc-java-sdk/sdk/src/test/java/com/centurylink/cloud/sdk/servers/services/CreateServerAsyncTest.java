package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.future.OperationFuture;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.tests.TestGroups;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.TestServerSupport.anyServerConfig;
import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

/**
 * @author ilya.drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class CreateServerAsyncTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    OperationFuture<ServerMetadata> createServerResponse;

    @Test
    public void testCreateServerAsync() throws Exception {
        ListenableFuture<OperationFuture<ServerMetadata>> future =
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
