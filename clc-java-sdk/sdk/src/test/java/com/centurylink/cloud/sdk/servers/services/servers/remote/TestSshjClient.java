package com.centurylink.cloud.sdk.servers.services.servers.remote;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.remote.SshjClient;
import com.centurylink.cloud.sdk.servers.services.domain.remote.domain.ShellResponse;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.TestGroups;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

/**
 * @author Anton Karavayeu
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class TestSshjClient extends AbstractServersSdkTest {
    private SshjClient sshjClient;

    @Inject
    private ServerService serverService;

    @Inject
    private ServerClient serverClient;

    @BeforeMethod
    public void init() {
        Server server = SingleServerFixture.server();
        ServerMetadata metadata = serverService.findByRef(server);
        sshjClient = new SshjClient.Builder()
                .host(getPublicIp(metadata))
                .username("root")
                .password(getPassword(metadata))
                .build();
    }

    private String getPublicIp(ServerMetadata metadata) {
        List<IpAddress> ipAddresses = metadata.getDetails().getIpAddresses();
        Optional<String> publicIp = ipAddresses.stream()
                .map(IpAddress::getPublicIp)
                .filter(Predicates.notNull())
                .findFirst();
        return publicIp.get();
    }

    private String getPassword(ServerMetadata metadata) {
        return serverClient.getServerCredentials(metadata.getId())
                .getPassword();
    }

    @Test()
    public void executeTest() throws Exception {
        OperationFuture<ShellResponse> response = sshjClient
                .run("ping -c 5 ya.ru")
                .run("echo test")
                .run("touch sdk")
                .execute();
        response.waitUntilComplete();
        assertTrue(response.getResult().getErrorStatus() != 1);
        assertNotNull(response.getResult().getTrace());
    }
}
