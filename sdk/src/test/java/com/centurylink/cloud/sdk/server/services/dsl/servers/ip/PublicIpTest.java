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

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.ModifyPublicIpConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static java.util.stream.Collectors.toList;

/**
 * @author aliaksandr krasitski
 */
@Test(groups = {RECORDED})
public class PublicIpTest extends AbstractServersSdkTest implements WireMockMixin {

    private Server serverRef;

    @Inject
    ServerService serverService;

    private List<IpAddress> getIpAddresses() {
        return serverService.findByRef(serverRef).getDetails().getIpAddresses();
    }

    @Test
    @WireMockFileSource("/add_ip_test")
    public void testAddPublicIp() {
        serverRef = Server.refById("de1altdtcrt960");

        //add public IP
        serverService
            .addPublicIp(serverRef.asFilter(), new CreatePublicIpConfig()
                .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
                .sourceRestrictions("70.100.60.140/32")
            )
            .waitUntilComplete();

        List<IpAddress> ipAddresses = getIpAddresses();

        ipAddresses.stream()
            .filter(address -> address.getPublicIp() != null)
            .forEach(address -> {
                PublicIpMetadata resp = serverService.getPublicIp(serverRef, address.getPublicIp());
                assertEquals(resp.getInternalIPAddress(), address.getInternal(), "internal ip addresses must be equal");
            });

        assertEquals(
            ipAddresses
                .stream()
                .filter(address -> address.getPublicIp() != null)
                .collect(toList())
                .size(),

            1,
            "public ip must be added"
        );

        int publicIpCount = ipAddresses.stream()
            .filter(address -> address.getPublicIp() != null)
            .collect(toList())
            .size();

        //find public IP by server
        List<PublicIpMetadata> publicIps = serverService.findPublicIp(serverRef);
        assertEquals(publicIpCount, publicIps.size());
    }

    @Test
    @WireMockFileSource("/modify_ip_test")
    public void modifyPublicIp() {
        serverRef = Server.refById("DE1ALTDIPTEST01");

        Integer[] ports = { 8081, 8888 };
        String[] sourceRestrictions = new String[]{ "50.50.50.50/32", "50.50.50.25/32" };

        ModifyPublicIpConfig config = new ModifyPublicIpConfig()
            .openPorts(ports)
            .sourceRestrictions(sourceRestrictions[0]);
        serverService.modifyPublicIp(serverRef.asFilter(), config).waitUntilComplete();

        config.sourceRestrictions(sourceRestrictions[1]);
        serverService.modifyPublicIp(serverRef, "66.155.4.95", config).waitUntilComplete();

        PublicIpMetadata updatedPublicIp = serverService.getPublicIp(serverRef, "66.155.4.95");

        List<Integer> updatedPorts = updatedPublicIp.getPorts()
            .stream()
            .map(port -> port.getPort())
            .collect(toList());
        assertTrue(updatedPorts.containsAll(Arrays.asList(ports)), "added ports must be present");

        List<String> updatedSourceRestrictions = updatedPublicIp.getSourceRestrictions()
            .stream()
            .map(restr -> restr.getCidr())
            .collect(toList());
        assertTrue(updatedSourceRestrictions.containsAll(Arrays.asList(sourceRestrictions)),
            "added source restriction must be present");
    }

    @Test
    @WireMockFileSource("/delete_ip_test")
    public void deletePublicIp() {
        serverRef = Server.refById("DE1ALTDIPTEST01");

        serverService
            .removePublicIp(serverRef.asFilter())
            .waitUntilComplete(Duration.ofMinutes(15));
    }

}
