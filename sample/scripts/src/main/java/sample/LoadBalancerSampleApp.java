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

package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerNodeMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMethod;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolPersistence;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerStatus;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.centurylink.cloud.sdk.server.services.client.domain.server.IpAddress;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType.CENTOS;
import static sample.SamplesTestsConstants.SAMPLES;

public class LoadBalancerSampleApp extends Assert {

    private LoadBalancerService loadBalancerService;
    private LoadBalancerPoolService loadBalancerPoolService;
    private LoadBalancerNodeService loadBalancerNodeService;
    private ServerService serverService;

    private static final DataCenter dataCenter = DataCenter.US_EAST_STERLING;


    public LoadBalancerSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider()
        );

        loadBalancerService = sdk.loadBalancerService();
        loadBalancerPoolService = sdk.loadBalancerPoolService();
        loadBalancerNodeService = sdk.loadBalancerNodeService();

        serverService = sdk.serverService();

    }

    @BeforeClass(groups = {SAMPLES})
    public void init() {
        clearAll();
    }

    @AfterClass(groups = {SAMPLES})
    public void clearAll () {
        deleteBalancers();
        deleteServers();
    }

    private void deleteBalancers() {
        loadBalancerService
            .delete(new LoadBalancerFilter().dataCenters(DataCenter.US_EAST_STERLING))
            .waitUntilComplete();
    }

    private void deleteServers() {
        serverService.delete(new ServerFilter().dataCenters(DataCenter.US_EAST_STERLING));
    }

    private LoadBalancerMetadata fetchLoadBalancerMetadata(LoadBalancer loadBalancer) {
        LoadBalancerMetadata metadata = loadBalancerService.findByRef(loadBalancer);
        assertNotNull(metadata);

        return metadata;
    }

    private LoadBalancerPoolMetadata fetchLoadBalancerPoolMetadata(LoadBalancerPool pool) {
        return loadBalancerPoolService.findByRef(pool);
    }

    private List<LoadBalancerMetadata> searchLoadBalancers(LoadBalancerFilter filter) {
        return loadBalancerService.find(filter);
    }

    private LoadBalancer createLoadBalancer(String name) {
        return loadBalancerService
            .create(
                new LoadBalancerConfig()
                    .name(name)
                    .description(name + " description")
                    .status(LoadBalancerStatus.ENABLED)
                    .dataCenter(dataCenter)
            )
            .waitUntilComplete()
            .getResult();
    }

    private LoadBalancerPool createLoadBalancerPool(LoadBalancer loadBalancer, Integer port) {
        return loadBalancerPoolService
            .create(
                new LoadBalancerPoolConfig()
                    .port(port)
                    .method(LoadBalancerPoolMethod.ROUND_ROBIN)
                    .persistence(LoadBalancerPoolPersistence.STANDARD)
                    .loadBalancer(loadBalancer)
            )
            .waitUntilComplete()
            .getResult();
    }

    private void setLoadBalancerNodes(LoadBalancerPool pool, List<LoadBalancerNodeMetadata> nodes) {
        loadBalancerNodeService
            .update(pool, nodes)
            .waitUntilComplete()
            .getResult();
    }

    private List<LoadBalancerNodeMetadata> composeNodeList(String publicIp) {
        List<LoadBalancerNodeMetadata> result = new ArrayList<>();

        LoadBalancerNodeMetadata node1 = new LoadBalancerNodeMetadata()
            .status(LoadBalancerStatus.ENABLED.getCode())
            .ipAddress(publicIp)
            .privatePort(8088);

        LoadBalancerNodeMetadata node2 = new LoadBalancerNodeMetadata()
            .status(LoadBalancerStatus.ENABLED.getCode())
            .ipAddress(publicIp)
            .privatePort(8089);

        result.add(node1);
        result.add(node2);

        return result;
    }

    @Test(groups = {SAMPLES})
    public void runSample() {
        List<LoadBalancerMetadata> loadBalancerMetadataList = searchLoadBalancers(
                new LoadBalancerFilter().dataCenters(DataCenter.US_EAST_STERLING)
        );

        assertEquals(loadBalancerMetadataList.size(), 0);

        LoadBalancer loadBalancer1 = createLoadBalancer("LB1");
        LoadBalancer loadBalancer2 = createLoadBalancer("LB2");

        LoadBalancerMetadata loadBalancer1Metadata = fetchLoadBalancerMetadata(loadBalancer1);
        LoadBalancerMetadata loadBalancer2Metadata = fetchLoadBalancerMetadata(loadBalancer2);

        assertNotNull(loadBalancer1Metadata);
        assertNotNull(loadBalancer2Metadata);

        loadBalancerMetadataList = searchLoadBalancers(
                new LoadBalancerFilter().dataCenters(DataCenter.US_EAST_STERLING)
        );

        assertEquals(loadBalancerMetadataList.size(), 2);

        LoadBalancerPool pool1 = createLoadBalancerPool(loadBalancer1, 80);
        LoadBalancerPool pool2 = createLoadBalancerPool(loadBalancer1, 443);

        LoadBalancerPoolMetadata pool1Metadata = fetchLoadBalancerPoolMetadata(pool1);
        LoadBalancerPoolMetadata pool2Metadata = fetchLoadBalancerPoolMetadata(pool2);

        assertNotNull(pool1Metadata);
        assertNotNull(pool2Metadata);
        assertEquals(pool1Metadata.getPort(), Integer.valueOf(80));
        assertEquals(pool2Metadata.getPort(), Integer.valueOf(443));

        assertEquals(pool1Metadata.getNodes().size(), 0);

        ServerMetadata serverMetadata = serverService.create(
            new CreateServerConfig()
                .name("tst")
                .description("desc")
                .group(Group.refByName(DataCenter.US_EAST_STERLING, Group.DEFAULT_GROUP))
                .type(STANDARD)
                .machine(new Machine()
                        .cpuCount(1)
                        .ram(1)
                )
                .template(Template.refByOs()
                        .dataCenter(DataCenter.US_EAST_STERLING)
                        .type(CENTOS)
                        .version("6")
                        .architecture(x86_64)
                )
                .timeToLive(ZonedDateTime.now().plusHours(2))
                .network(
                    new NetworkConfig()
                        .publicIpConfig(
                            new CreatePublicIpConfig()
                                .openPorts(new PortConfig().port(77))
                        )
                )
        )
        .waitUntilComplete().getResult();

        serverMetadata = serverService.findByRef(serverMetadata.asRefById());

        String publicIp = serverMetadata.getDetails().getIpAddresses().stream()
            .map(IpAddress::getPublicIp)
            .filter(Objects::nonNull)
            .findFirst()
            .get();

        setLoadBalancerNodes(pool1, composeNodeList(publicIp));

        pool1Metadata = fetchLoadBalancerPoolMetadata(pool1);
        assertEquals(pool1Metadata.getNodes().size(), 2);
    }
}
