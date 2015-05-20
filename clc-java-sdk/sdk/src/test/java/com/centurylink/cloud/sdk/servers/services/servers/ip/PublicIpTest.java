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

package com.centurylink.cloud.sdk.servers.services.servers.ip;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.ModifyPublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author aliaksandr krasitski
 */
@Test
public class PublicIpTest extends AbstractServersSdkTest {

    private Server serverRef;

    @Inject
    ServerService serverService;

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testPublicIp() {
        serverRef = SingleServerFixture.server();

        addPublicIp();

        modifyPublicIp();

        deletePublicIp();
    }

    private void checkServerStarted() {
        ServerMetadata serverMetadata = serverService.findByRef(serverRef);
        if (!serverMetadata.getDetails().getPowerState().equals("started")) {
            serverService.powerOn(serverRef);
        }
        if (!serverMetadata.getStatus().equals("active")) {
            throw new RuntimeException("server " + serverMetadata.getId() + " is not in state active");
        }
    }

    private List<IpAddress> getIpAddresses() {
        return serverService.findByRef(serverRef).getDetails().getIpAddresses();
    }

    private void addPublicIp() {
        assertEquals(serverService.findPublicIp(serverRef).size(), 0, "after server creation public ip doesn't exist");

        checkServerStarted();

        //add public IP
        serverService
            .addPublicIp(serverRef.asFilter(),
                new CreatePublicIpConfig()
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

        assertEquals(ipAddresses.stream().filter(address -> address.getPublicIp() != null).collect(toList()).size(), 1,
            "public ip must be added");

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
        assertTrue(updatedSourceRestrictions.containsAll(Arrays.asList(sourceRestrictions)),
            "added source restriction must be present");
    }

    private void deletePublicIp() {
        serverService.removePublicIp(serverRef.asFilter()).waitUntilComplete();

        List<IpAddress> initialIpAddresses = getIpAddresses();

        assertEquals(initialIpAddresses.stream()
            .filter(notNull())
            .filter(addr -> addr.getPublicIp() != null).count(), 0, "count of public IPs must be 0 after clearing");
    }

}
