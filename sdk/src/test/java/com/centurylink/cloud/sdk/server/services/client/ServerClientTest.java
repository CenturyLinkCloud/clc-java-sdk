package com.centurylink.cloud.sdk.server.services.client;

import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ServerCredentials;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
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
