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
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerStatus;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("update")
public class UpdateLoadBalancerTest extends AbstractLoadBalancerSdkTest implements WireMockMixin {

    @Inject
    LoadBalancerService loadBalancerService;

    LoadBalancer loadBalancer;

    @Test
    public void testUpdate() {
        String name = "Balancer2";
        String description = "Balancer2 description";
        LoadBalancerStatus status = LoadBalancerStatus.ENABLED;

        String updatedName = "Balancer2-upd";
        String updatedDescription = "Balancer2-upd description";
        LoadBalancerStatus updatedStatus = LoadBalancerStatus.DISABLED;

        loadBalancer = loadBalancerService
            .create(
                new LoadBalancerConfig()
                    .name(name)
                    .description(description)
                    .status(status)
                    .dataCenter(DataCenter.US_EAST_STERLING)
            )
            .waitUntilComplete()
            .getResult();

        loadBalancerService
            .update(
                new LoadBalancerFilter().loadBalancers(loadBalancer),
                new LoadBalancerConfig()
                    .name(updatedName)
                    .description(updatedDescription)
                    .status(updatedStatus)
            )
            .waitUntilComplete()
            .getResult();

        LoadBalancerMetadata loadBalancerMetadata = loadBalancerService.findByRef(loadBalancer);

        assertEquals(loadBalancerMetadata.getName(), updatedName);
        assertEquals(loadBalancerMetadata.getDescription(), updatedDescription);
        assertEquals(loadBalancerMetadata.getStatus(), updatedStatus.getCode());
    }

    @AfterMethod
    public void deleteBalancer() {
        loadBalancerService.delete(loadBalancer.asFilter());
    }
}
