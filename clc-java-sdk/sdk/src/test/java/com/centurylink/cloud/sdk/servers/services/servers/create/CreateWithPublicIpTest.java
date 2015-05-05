package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Ilya Drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class CreateWithPublicIpTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    ServerMetadata server;

    @Test
    public void testCreateServerWithPublicIp() throws Exception {
        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("CTTL")
                .network(new NetworkConfig()
                    .publicIpConfig(new CreatePublicIpConfig()
                        .openPorts(8080)
                    )
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(server.getId());
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asRefById().asFilter());
    }

}
