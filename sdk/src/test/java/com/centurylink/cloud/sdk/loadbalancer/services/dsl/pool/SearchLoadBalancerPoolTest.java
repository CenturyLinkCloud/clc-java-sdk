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
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerPoolFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;


@Test(groups = {RECORDED})
@WireMockFileSource("search")
public class SearchLoadBalancerPoolTest extends AbstractLoadBalancerSdkTest implements WireMockMixin {

    @Inject
    LoadBalancerPoolService loadBalancerPoolService;

    @Test
    public void testFindByLoadBalancerAndDatacenter() {
        String loadBalancerId = "32ba7c6bb3a24784b18f14cb2db9201a";
        DataCenter dataCenter = DataCenter.DE_FRANKFURT;

        LoadBalancerPoolFilter loadBalancerPoolFilter = new LoadBalancerPoolFilter(
            new LoadBalancerFilter()
                .dataCenters(dataCenter)
                .id(loadBalancerId),
            alwaysTrue()
        );

        List<LoadBalancerPoolMetadata> loadBalancersPoolList = loadBalancerPoolService.find(
                loadBalancerPoolFilter
        );

        assertNotNull(loadBalancersPoolList);
        assertEquals(loadBalancersPoolList.size(), 1);
        assertEquals(loadBalancersPoolList.get(0).getLoadBalancerId(), loadBalancerId);
    }

    @Test
    public void testFindByIdAndLoadBalancer() {
        String poolId = "57de6742c8e442e393f7bb83b916567c";
        String loadBalancerId = "32ba7c6bb3a24784b18f14cb2db9201a";
        DataCenter dataCenter = DataCenter.DE_FRANKFURT;

        LoadBalancerPoolMetadata loadBalancerPoolMetadata = loadBalancerPoolService.findByRef(
            LoadBalancerPool.refById(
                poolId,
                LoadBalancer.refById(loadBalancerId, dataCenter)
            )
        );

        assertNotNull(loadBalancerPoolMetadata);
        assertNotNull(loadBalancerPoolMetadata.getId(), poolId);
        assertNotNull(loadBalancerPoolMetadata.getLoadBalancerId(), loadBalancerId);
    }

    @Test
    public void testFindAll() {
        List<LoadBalancerPoolMetadata> metadataList = loadBalancerPoolService.find(
                new LoadBalancerPoolFilter()
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 2);
    }
}
