package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.ip.CreatePublicIpRequest;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConverter;
import com.centurylink.cloud.sdk.servers.services.domain.ip.Subnet;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortRangeConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.apache.commons.net.util.SubnetUtils;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author aliaksandr krasitski
 */
@Test
public class PublicIpTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    private long countOfPublicIp(List<IpAddress> ipAddresses) {
        return ipAddresses.stream()
            .filter(address -> address.getPublicIp() != null)
            .count();
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testPublicIpTest() {
        Server serverRef = SingleServerFixture.server();

        serverService
            .addPublicIp(serverRef,
                new PublicIpConfig()
                    .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
                    .sourceRestrictions("70.100.60.140/32")
            )
            .waitUntilComplete();

        List<IpAddress> ipAddresses = serverService.findByRef(serverRef).getDetails().getIpAddresses();

        assertEquals(countOfPublicIp(ipAddresses), 1);
        ipAddresses.stream()
                .filter(address -> address.getPublicIp() != null)
                .forEach(address -> {
                    PublicIpMetadata resp = serverService.getPublicIp(serverRef, address.getPublicIp());
                    assertEquals(resp.getInternalIPAddress(), address.getInternal(), "internal ip addresses must be equal");
                });

        int publicIpCount = ipAddresses.stream()
                .filter(address -> address.getPublicIp() != null)
                .collect(toList())
                .size();

        List<PublicIpMetadata> publicIps = serverService.findPublicIp(serverRef);
        assertEquals(publicIpCount, publicIps.size());

        serverService.removePublicIp(serverRef).waitUntilComplete();

        List<IpAddress> initialIpAddresses = serverService.findByRef(serverRef).getDetails().getIpAddresses();

        assertEquals(countOfPublicIp(initialIpAddresses), 0, "count of public IP addresses must be 0");
    }

    @Test
    public void testPublicIpConverter() {
        PublicIpConverter converter = new PublicIpConverter();

        String internalIpAddress = "10.6.10.6";
        String cidr = "100.0.50.1/17";
        int portFrom = 8080;
        int portTo = 8888;
        PublicIpConfig config = new PublicIpConfig()
                .internalIpAddress(internalIpAddress)
                .openPorts(new PortConfig().port(portFrom).to(portTo))
                .sourceRestrictions(new Subnet().cidr(cidr));

        CreatePublicIpRequest req = converter.createPublicIpRequest(config);

        assertEquals(req.getInternalIPAddress(), config.getInternalIpAddress(), "check internal ip address");
        assertEquals(req.getPorts().size(), config.getPorts().size(), "check ports count");
        assertEquals(req.getPorts().get(0).getProtocol(), config.getPorts().get(0).getProtocolType().name(), "check protocol type");
        assertEquals(req.getPorts().get(0).getPort(), config.getPorts().get(0).getPort(), "check port range from");
        assertEquals(req.getPorts().get(0).getPortTo(), (((PortRangeConfig)config.getPorts().get(0)).getPortTo()), "check port range to");

        assertEquals(req.getSourceRestrictions().size(), config.getRestrictions().size(), "check source restrictions count");
        assertEquals(req.getSourceRestrictions().get(0).getCidr(), config.getRestrictions().get(0).getCidr(), "check source restriction in cidr format");
    }

    @Test
    public void testSubnet() {
        String ipAddress = "10.6.10.6";
        String cidr = "100.0.50.1/17";
        String mask = "255.0.0.0";
        String cidrMask = "/17";

        Subnet subnet = new Subnet().cidrMask(cidrMask);
        assertEquals(subnet.getCidr(), null, "cidr should be null because ipAddress not specified");

        subnet.mask(mask);
        assertEquals(subnet.getCidr(), null, "cidr should be null because ipAddress not specified");

        subnet.ipAddress(ipAddress);
        assertEquals(subnet.getCidr(), new SubnetUtils(ipAddress, mask).getInfo().getCidrSignature(), "check constructing subnet from ipAddress and net mask");

        subnet = new Subnet().ipAddress(ipAddress).cidrMask(cidrMask);
        assertEquals(subnet.getCidr(), new SubnetUtils(ipAddress+cidrMask).getInfo().getCidrSignature(), "check constructing subnet from ipAddress and cidrMask");
    }

}
