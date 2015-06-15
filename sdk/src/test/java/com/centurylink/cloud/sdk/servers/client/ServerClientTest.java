package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.ServerCredentials;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

/**
 * @author Anton Karavayeu
 */
public class ServerClientTest extends AbstractServersSdkTest {

    @Inject
    private ServerService serverService;

    @Inject
    private ServerClient serverClient;

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void getServerCredentialsTest() {
        ServerMetadata server = serverService.findByRef(SingleServerFixture.server());

        ServerCredentials credentials = serverClient.getServerCredentials(server.getName());

        assertNotNull(credentials);
        assertNotNull(credentials.getPassword());
        assertNotNull(credentials.getUserName());
        assertFalse(credentials.getPassword().isEmpty());
        assertFalse(credentials.getUserName().isEmpty());
    }
}
