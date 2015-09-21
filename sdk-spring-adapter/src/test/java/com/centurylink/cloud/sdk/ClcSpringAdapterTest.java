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

package com.centurylink.cloud.sdk;

import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.StatisticsService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.TestGroup.SPRING_ADAPTER;

public class ClcSpringAdapterTest extends Assert {

    @Test(groups = {SPRING_ADAPTER})
    public void runTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);

        GroupService groupService = context.getBean(GroupService.class);
        ServerService serverService = context.getBean(ServerService.class);
        PolicyService policyService = context.getBean(PolicyService.class);
        TemplateService templateService = context.getBean(TemplateService.class);
        DataCenterService dataCenterService = context.getBean(DataCenterService.class);
        StatisticsService statisticsService = context.getBean(StatisticsService.class);
        LoadBalancerService loadBalancerService = context.getBean(LoadBalancerService.class);
        LoadBalancerPoolService loadBalancerPoolService = context.getBean(LoadBalancerPoolService.class);
        LoadBalancerNodeService loadBalancerNodeService = context.getBean(LoadBalancerNodeService.class);

        assertNotNull(groupService);
        assertNotNull(serverService);
        assertNotNull(policyService);
        assertNotNull(templateService);
        assertNotNull(dataCenterService);
        assertNotNull(statisticsService);
        assertNotNull(loadBalancerService);
        assertNotNull(loadBalancerPoolService);
        assertNotNull(loadBalancerNodeService);

        List<LoadBalancerMetadata> loadBalancerMetadataList = loadBalancerService
            .find(
                new LoadBalancerFilter()
                    .dataCenters(DataCenter.US_EAST_STERLING)
            );

        assertNotNull(loadBalancerMetadataList);
    }
}
