package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.ModifyPublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConverter;
import com.centurylink.cloud.sdk.servers.services.domain.ip.Subnet;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortRangeConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.apache.commons.net.util.SubnetUtils;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.core.services.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author aliaksandr krasitski
 */
@Test
public class PublicIpTest extends AbstractServersSdkTest {

    private ServerRef serverRef;

    @Inject
    ServerService serverService;

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testPublicIp() {
        new SingleServerFixture().createServer();
        serverRef = SingleServerFixture.server();

        addPublicIp();

        modifyPublicIp();

        deletePublicIp();
    }

    private void checkServerPowerOn() {
        if (!serverService.findByRef(serverRef).getDetails().getPowerState().equals("started")) {
            serverService.powerOn(serverRef);
        }
    }

    private List<IpAddress> getIpAddresses() {
        return serverService.findByRef(serverRef).getDetails().getIpAddresses();
    }

    private void addPublicIp() {
        assertEquals(serverService.findPublicIp(serverRef).size(), 0, "after server creation public ip doesn't exist");

        checkServerPowerOn();

        //add public IP
        serverService
            .addPublicIp(serverRef,
                new CreatePublicIpConfig()
                    .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
                    .sourceRestrictions("70.100.60.140/32")
            ).waitUntilComplete();

        List<IpAddress> ipAddresses = getIpAddresses();

        ipAddresses.stream()
            .filter(address -> address.getPublicIp() != null)
            .forEach(address -> {
                PublicIpMetadata resp = serverService.getPublicIp(serverRef, address.getPublicIp());
                assertEquals(resp.getInternalIPAddress(), address.getInternal(), "internal ip addresses must be equal");
            });

        assertEquals(ipAddresses.stream().filter(address -> address.getPublicIp() != null).collect(toList()).size(), 1, "public ip must be added");

        int publicIpCount = ipAddresses.stream()
            .filter(address -> address.getPublicIp() != null)
            .collect(toList())
            .size();

        //find public IP by server
        List<PublicIpMetadata> publicIps = serverService.findPublicIp(serverRef);
        assertEquals(publicIpCount, publicIps.size());
    }

    private void modifyPublicIp() {
        List<IpAddress> ipAddresses = getIpAddresses();

        Integer[] ports = {8081, 8888};
        String[] sourceRestrictions = new String[]{"50.50.50.50/32", "50.50.50.25/32"};
        String publicIpAddress = ipAddresses.stream()
            .filter(address -> address.getPublicIp() != null)
            .map(address -> address.getPublicIp())
            .findFirst()
            .get();

        ModifyPublicIpConfig config = new ModifyPublicIpConfig()
            .openPorts(ports)
            .sourceRestrictions(sourceRestrictions[0]);
        serverService.modifyPublicIp(serverRef.asFilter(), config).waitUntilComplete();

        config.sourceRestrictions(sourceRestrictions[1]);
        serverService.modifyPublicIp(serverRef, publicIpAddress, config).waitUntilComplete();

        PublicIpMetadata updatedPublicIp = serverService.getPublicIp(serverRef, publicIpAddress);

        List<Integer> updatedPorts = updatedPublicIp.getPorts()
            .stream()
            .map(port -> port.getPort())
            .collect(toList());
        assertTrue(updatedPorts.containsAll(Arrays.asList(ports)), "added ports must be present");

        List<String> updatedSourceRestrictions = updatedPublicIp.getSourceRestrictions()
            .stream()
            .map(restr -> restr.getCidr())
            .collect(toList());
        assertTrue(updatedSourceRestrictions.containsAll(Arrays.asList(sourceRestrictions)), "added source restriction must be present");
    }

    private void deletePublicIp() {
        serverService.removePublicIp(serverRef).waitUntilComplete();

        List<IpAddress> initialIpAddresses = getIpAddresses();

        assertEquals(initialIpAddresses.stream()
            .filter(notNull())
            .filter(addr -> addr.getPublicIp() != null).count(), 0, "count of public IPs must be 0 after clearing");
    }

    @Test
    public void testPublicIpConverter() {
        PublicIpConverter converter = new PublicIpConverter();

        String internalIpAddress = "10.6.10.6";
        String cidr = "100.0.50.1/17";
        int portFrom = 8080;
        int portTo = 8888;
        CreatePublicIpConfig config = new CreatePublicIpConfig()
            .internalIpAddress(internalIpAddress)
            .openPorts(new PortConfig().port(portFrom).to(portTo))
            .sourceRestrictions(new Subnet().cidr(cidr));

        PublicIpRequest req = converter.createPublicIpRequest(config);

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
