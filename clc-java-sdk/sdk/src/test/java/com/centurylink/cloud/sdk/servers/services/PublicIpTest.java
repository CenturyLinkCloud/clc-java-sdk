package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.ProtocolType;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpAddressRequest;
import com.centurylink.cloud.sdk.servers.services.domain.ip.SourceRestriction;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author aliaksandr krasitski
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class PublicIpTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Test
    public void testPublicIpTest() {
        ServerRef serverRef = SingleServerFixture.server();
        ServerMetadata server = serverService.findByRef(serverRef);

        Link response = serverService
            .addPublicIp(server.getId(),
                new PublicIpAddressRequest()
                    .ports(
                        PortConfig.HTTPS,
                        PortConfig.HTTP
                    )
                    .sourceRestrictions("70.100.60.140/32")
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(response.getId());

        List<IpAddress> ipAddresses = server.getDetails().getIpAddresses();
        ipAddresses.parallelStream().forEach(address -> {
            if (address.getPublicIp() != null) {
                serverService.getPublicIp(server.getId(), address.getPublicIp());
            }
        });
    }

}
