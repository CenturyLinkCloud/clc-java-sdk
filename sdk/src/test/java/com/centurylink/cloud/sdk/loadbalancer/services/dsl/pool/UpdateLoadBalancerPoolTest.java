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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.pool;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.loadbalancer.services.AbstractLoadBalancerSdkTest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMethod;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolPersistence;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("update")
public class UpdateLoadBalancerPoolTest extends AbstractLoadBalancerSdkTest implements WireMockMixin {

    @Inject
    LoadBalancerPoolService loadBalancerPoolService;

    LoadBalancerPool loadBalancerPool;

    @Test
    public void testUpdate() {
        LoadBalancer loadBalancer = LoadBalancer.refById(
                "32ba7c6bb3a24784b18f14cb2db9201a",
                DataCenter.DE_FRANKFURT
        );

        loadBalancerPool = loadBalancerPoolService
                .create(
                    new LoadBalancerPoolConfig()
                        .port(80)
                        .method(LoadBalancerPoolMethod.ROUND_ROBIN)
                        .persistence(LoadBalancerPoolPersistence.STANDARD)
                        .loadBalancer(loadBalancer)
                )
                .waitUntilComplete()
                .getResult();

        loadBalancerPool = loadBalancerPoolService
                .update(
                    loadBalancerPool,
                    new LoadBalancerPoolConfig()
                            .method(LoadBalancerPoolMethod.LEAST_CONNECTION)
                            .persistence(LoadBalancerPoolPersistence.STICKY)
                            .loadBalancer(loadBalancer)
                )
                .waitUntilComplete()
                .getResult();

        LoadBalancerPoolMetadata metadata = loadBalancerPoolService.findByRef(loadBalancerPool);

        assertNotNull(metadata);
        assertEquals(metadata.getMethod(), LoadBalancerPoolMethod.LEAST_CONNECTION.getCode());
        assertEquals(metadata.getPersistence(), LoadBalancerPoolPersistence.STICKY.getCode());
    }

    @AfterMethod
    public void deletePool() {
        loadBalancerPoolService.delete(loadBalancerPool.asFilter());
    }
}
