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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.node;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.loadbalancer.services.AbstractLoadBalancerSdkTest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerNodeMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerStatus;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("update")
public class UpdateLoadBalancerNodeTest extends AbstractLoadBalancerSdkTest implements WireMockMixin {

    @Inject
    LoadBalancerNodeService loadBalancerNodeService;

    @Inject
    LoadBalancerPoolService loadBalancerPoolService;

    @Test
    public void testUpdate() {
        LoadBalancer loadBalancer = LoadBalancer.refById(
                "b4c06dcc5d3842f29b7ac51cbb46af2b",
                DataCenter.DE_FRANKFURT
        );

        LoadBalancerPool pool = LoadBalancerPool.refById(
                "fd7dd87eadb94655a583a7d42d170e56",
                loadBalancer
        );


        loadBalancerNodeService
            .update(pool, composeNodesToBeUpdated())
            .waitUntilComplete()
            .getResult();

        LoadBalancerPoolMetadata metadata = loadBalancerPoolService.findByRef(pool);

        assertNotNull(metadata);
        assertEquals(metadata.getNodes().size(), 2);

        LoadBalancerNodeMetadata node1 = metadata.getNodes().get(0);
        LoadBalancerNodeMetadata node2 = metadata.getNodes().get(1);

        assertEquals(node1.getStatus(), LoadBalancerStatus.ENABLED.getCode());
        assertEquals(node1.getIpAddress(), "66.155.4.73");
        assertEquals(node1.getPrivatePort(), Integer.valueOf(8088));

        assertEquals(node2.getStatus(), LoadBalancerStatus.ENABLED.getCode());
        assertEquals(node2.getIpAddress(), "66.155.4.73");
        assertEquals(node2.getPrivatePort(), Integer.valueOf(8089));
    }

    private List<LoadBalancerNodeMetadata> composeNodesToBeUpdated() {
        List<LoadBalancerNodeMetadata> result = new ArrayList<>();

        LoadBalancerNodeMetadata node1 = new LoadBalancerNodeMetadata() {{
            setStatus(LoadBalancerStatus.ENABLED.getCode());
            setIpAddress("66.155.4.73");
            setPrivatePort(8088);
        }};

        LoadBalancerNodeMetadata node2 = new LoadBalancerNodeMetadata() {{
            setStatus(LoadBalancerStatus.ENABLED.getCode());
            setIpAddress("66.155.4.73");
            setPrivatePort(8089);
        }};

        result.add(node1);
        result.add(node2);

        return result;
    }
}
