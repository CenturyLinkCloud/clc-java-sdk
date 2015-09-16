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
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.CredentialsProvider;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.InvoiceService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.StatisticsService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClcSpringConfig {

    private ClcSdk clcSdk;

    @Autowired(required = false)
    public void setSdk(ClcSdk clcSdk) {
        this.clcSdk = clcSdk != null ? clcSdk : new ClcSdk();
    }

    @Bean
    public ServerService serverService() {
        return clcSdk.serverService();
    }

    @Bean
    public GroupService groupService() {
        return clcSdk.groupService();
    }

    @Bean
    public TemplateService templateService() {
        return clcSdk.templateService();
    }

    @Bean
    public DataCenterService dataCenterService() {
        return clcSdk.dataCenterService();
    }

    @Bean
    public CredentialsProvider getCredentialsProvider() {
        return clcSdk.getCredentialsProvider();
    }

    @Bean
    public StatisticsService statisticsService() {
        return clcSdk.statisticsService();
    }

    @Bean
    public PolicyService policyService() {
        return clcSdk.policyService();
    }

    @Bean
    public InvoiceService invoiceService() {
        return clcSdk.invoiceService();
    }

    @Bean
    public LoadBalancerService loadBalancerService() {
        return clcSdk.loadBalancerService();
    }

    @Bean
    public LoadBalancerPoolService loadBalancerPoolService() {
        return clcSdk.loadBalancerPoolService();
    }

    @Bean
    public LoadBalancerNodeService loadBalancerNodeService() {
        return clcSdk.loadBalancerNodeService();
    }
}
