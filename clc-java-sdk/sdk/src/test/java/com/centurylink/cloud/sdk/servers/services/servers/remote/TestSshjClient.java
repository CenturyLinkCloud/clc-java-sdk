package com.centurylink.cloud.sdk.servers.services.servers.remote;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.domain.remote.SshjClient;
import com.centurylink.cloud.sdk.servers.services.domain.remote.domain.ShellResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Anton Karavayeu
 */
public class TestSshjClient extends AbstractServersSdkTest {
    private SshjClient sshjClient;

    @BeforeClass
    public void init() {
        sshjClient = new SshjClient.Builder()
                .host("66.155.4.130")
                .username("root")
                .password("D@eCxAH8?=#.e}={")
                .build();
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
