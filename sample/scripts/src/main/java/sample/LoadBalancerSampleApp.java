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
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static sample.SamplesTestsConstants.SAMPLES;

public class LoadBalancerSampleApp extends Assert {

    private LoadBalancerService loadBalancerService;
    private LoadBalancerPoolService loadBalancerPoolService;
    private LoadBalancerNodeService loadBalancerNodeService;

    private final DataCenter dataCenter = DataCenter.US_EAST_STERLING;


    public LoadBalancerSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider()
        );

        loadBalancerService = sdk.loadBalancerService();
        loadBalancerPoolService = sdk.loadBalancerPoolService();
        loadBalancerNodeService = sdk.loadBalancerNodeService();

    }

    @BeforeClass(groups = {SAMPLES})
    public void init() {
        clearAll();
    }

    @AfterClass(groups = {SAMPLES})
    public void deleteBalancers() {
        clearAll();
    }

    private void clearAll() {
        loadBalancerService
            .delete(new LoadBalancerFilter().dataCenters(DataCenter.US_EAST_STERLING))
            .waitUntilComplete();
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

    private List<LoadBalancerNodeMetadata> composeNodeList() {
        List<LoadBalancerNodeMetadata> result = new ArrayList<>();

        LoadBalancerNodeMetadata node1 = new LoadBalancerNodeMetadata() {{
            setStatus(LoadBalancerStatus.ENABLED.getCode());
            setIpAddress("10.135.238.12");
            setPrivatePort(8088);
        }};

        LoadBalancerNodeMetadata node2 = new LoadBalancerNodeMetadata() {{
            setStatus(LoadBalancerStatus.ENABLED.getCode());
            setIpAddress("10.135.238.12");
            setPrivatePort(8089);
        }};

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
        setLoadBalancerNodes(pool1, composeNodeList());

        pool1Metadata = loadBalancerPoolService.findByRef(pool1);
        assertEquals(pool1Metadata.getNodes().size(), 2);
    }
}
