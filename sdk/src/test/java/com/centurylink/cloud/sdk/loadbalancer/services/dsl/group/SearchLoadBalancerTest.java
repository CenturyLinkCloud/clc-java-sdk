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
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("search")
public class SearchLoadBalancerTest extends AbstractLoadBalancerSdkTest implements WireMockMixin {

    @Inject
    LoadBalancerService loadBalancerService;

    @Test
    public void testFindByDataCenter() {
        List<LoadBalancerMetadata> loadBalancersList = loadBalancerService.find(
                new LoadBalancerFilter().dataCenters(DataCenter.DE_FRANKFURT)
        );

        assertNotNull(loadBalancersList);
        assertEquals(loadBalancersList.size(), 5);
    }

    @Test
    public void testFindByRefs() {
        String balancerName = "Balancer";

        List<LoadBalancerMetadata> loadBalancersList = loadBalancerService.find(
            new LoadBalancerFilter()
                .dataCenters(DataCenter.DE_FRANKFURT.getId())
                .loadBalancers(LoadBalancer.refByName(balancerName, DataCenter.DE_FRANKFURT))
        );

        assertNotNull(loadBalancersList);
        loadBalancersList.forEach(
            balancer -> assertEquals(balancer.getName(), balancerName)
        );
    }

    @Test
    public void testFindByNameContains() {
        List<LoadBalancerMetadata> loadBalancersList = loadBalancerService.find(
                new LoadBalancerFilter().nameContains("Balancer1")
        );

        assertNotNull(loadBalancersList);
        assertEquals(loadBalancersList.size(), 2);
    }

    @Test
    public void testFindByIdAndDatacenter() {
        String id = "b4c06dcc5d3842f29b7ac51cbb46af2b";

        LoadBalancerMetadata loadBalancer = loadBalancerService.findByRef(
                LoadBalancer.refById(id, DataCenter.DE_FRANKFURT)
        );

        assertNotNull(loadBalancer);
        assertEquals(loadBalancer.getId(), id);
    }

    @Test
    public void testFindAll() {
        List<LoadBalancerMetadata> loadBalancersList = loadBalancerService.find(
                new LoadBalancerFilter()
        );

        assertNotNull(loadBalancersList);
        assertEquals(loadBalancersList.size(), 6);
    }
}
