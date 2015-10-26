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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.group;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.loadbalancer.services.AbstractLoadBalancerSdkTest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerNodeMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerNodeFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerPoolFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
public class UpdateLoadBalancerWithPoolsAndNodesTest extends AbstractLoadBalancerSdkTest implements WireMockMixin {

    @Inject
    LoadBalancerService loadBalancerService;

    @Inject
    LoadBalancerPoolService loadBalancerPoolService;

    @Inject
    LoadBalancerNodeService loadBalancerNodeService;

    LoadBalancer loadBalancer;

    @Test
    @WireMockFileSource("update-super/update")
    public void testUpdate() {
        String name = "Super Balancer";
        String description = "Super Balancer description";

        loadBalancer = loadBalancerService
            .update(
                new LoadBalancerFilter().names(name),
                new LoadBalancerConfig()
                    .name(name + " updated")
                    .description(description + " updated")
                    .dataCenter(DataCenter.GB_PORTSMOUTH)
                    .pool(
                        new LoadBalancerPoolConfig()
                            .port(80)
                            .nodes(
                                new LoadBalancerNodeMetadata()
                                    .ipAddress("66.155.18.22")
                                    .privatePort(8085)
                            )
                    )
            )
            .waitUntilComplete()
            .getResult()
            .get(0);
    }

    @Test
    @WireMockFileSource("update-super/assert")
    public void testUpdateAssert() {
        LoadBalancerMetadata metadata = loadBalancerService.findByRef(loadBalancer);

        assertNotNull(metadata);

        List<LoadBalancerPoolMetadata> poolList =
            loadBalancerPoolService.find(new LoadBalancerPoolFilter().loadBalancers(loadBalancer));

        assertEquals(poolList.size(), 2);
        poolList.forEach(pool -> {
            if (pool.getPort().equals(80)) {
                List<LoadBalancerNodeMetadata> nodes = loadBalancerNodeService.find(
                    new LoadBalancerNodeFilter()
                        .loadBalancerPools(
                            LoadBalancerPool.refById(pool.getId(), loadBalancer)
                        )
                );

                assertEquals(nodes.size(), 1);
            }
        });
    }

    @AfterClass
    public void deleteBalancer() {
        loadBalancerService.delete(loadBalancer.asFilter());
    }
}
