package com.centurylink.cloud.sdk.servers.services.servers.ip;

import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConverter;
import com.centurylink.cloud.sdk.servers.services.domain.ip.Subnet;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortRangeConfig;
import org.apache.commons.net.util.SubnetUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by aliaksandr.krasitski on 4/28/2015.
 */
public class PublicIpUtilTest {

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
