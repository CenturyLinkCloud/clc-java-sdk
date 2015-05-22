package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.ServerCredentials;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

/**
 * @author Anton Karavayeu
 */
public class ServerClientTest extends AbstractServersSdkTest {

    @Inject
    private ServerClient serverClient;

    @Test(groups = INTEGRATION)
    public void getServerCredentialsTest() {
        ServerCredentials credentials = serverClient.getServerCredentials("DE1ALTRATL101");
        assertNotNull(credentials);
        assertNotNull(credentials.getPassword());
        assertNotNull(credentials.getUserName());
        assertFalse(credentials.getPassword().isEmpty());
        assertFalse(credentials.getUserName().isEmpty());
    }
}
