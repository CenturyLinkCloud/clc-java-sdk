package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PortConfig;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.servers.client.domain.server.PublicIpAddressResponse;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

/**
 * @author aliaksandr krasitski
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class PublicIpTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    public void testPublicIpTest() {
        new SingleServerFixture().createServer();
        ServerRef serverRef = SingleServerFixture.server();

        serverService
            .addPublicIp(serverRef,
                new PublicIpMetadata()
                    .ports(
                        PortConfig.HTTPS,
                        PortConfig.HTTP
                    )
                    .sourceRestrictions("70.100.60.140/32")
            );

        List<IpAddress> ipAddresses = serverService.findByRef(serverRef).getDetails().getIpAddresses();

        ipAddresses.stream()
                .forEach(address -> {
                    if (address.getPublicIp() != null) {
                        PublicIpAddressResponse resp = serverService.getPublicIp(serverRef, address.getPublicIp());
                        assertEquals(resp.getInternalIPAddress(), address.getInternal(), "internal ip addresses must be equal");
                    }
                });

        serverService.removePublicIp(serverRef).waitUntilComplete();

        List<IpAddress> initialIpAddresses = serverService.findByRef(serverRef).getDetails().getIpAddresses();

        assertEquals(initialIpAddresses.stream().filter(addr -> addr.getPublicIp() != null).count(), 0, "count of public IP addresses must be 0");
    }

}
