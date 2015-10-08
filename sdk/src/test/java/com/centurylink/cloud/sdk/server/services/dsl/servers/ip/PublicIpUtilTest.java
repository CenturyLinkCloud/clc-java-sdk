/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.server.services.dsl.servers.ip;

import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.PublicIpConverter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.Subnet;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.SubnetUtils;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortRangeConfig;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author aliaksandr.krasitski
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
        String mask = "255.0.0.0";
        String cidrMask = "/17";

        Subnet subnet = new Subnet().cidrMask(cidrMask);
        assertEquals(subnet.getCidr(), null, "cidr should be null because ipAddress not specified");

        subnet.mask(mask);
        assertEquals(subnet.getCidr(), null, "cidr should be null because ipAddress not specified");

        subnet.ipAddress(ipAddress);
        assertEquals(subnet.getCidr(), new SubnetUtils(ipAddress, mask).getCidrSignature(), "check constructing subnet from ipAddress and net mask");

        subnet = new Subnet().ipAddress(ipAddress).cidrMask(cidrMask);
        assertEquals(subnet.getCidr(), new SubnetUtils(ipAddress+cidrMask).getCidrSignature(), "check constructing subnet from ipAddress and cidrMask");
    }
}
