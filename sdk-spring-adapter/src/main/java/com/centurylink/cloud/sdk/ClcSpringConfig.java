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
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.policy.services.dsl.AutoscalePolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.FirewallPolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.InvoiceService;
import com.centurylink.cloud.sdk.server.services.dsl.NetworkService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.StatisticsService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClcSpringConfig implements InitializingBean {

    private CredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
    private SdkConfiguration sdkConfig = SdkConfiguration.DEFAULT;
    private ClcSdk sdk;

    @Autowired(required = false)
    public void setClcSdkConfig(SdkConfiguration sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    @Autowired(required = false)
    public void setClcCredentialsProvider(CredentialsProvider provider) {
        this.credentialsProvider = provider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sdk = new ClcSdk(credentialsProvider, sdkConfig);
    }

    @Bean
    public ServerService clcServerService() {
        return sdk.serverService();
    }

    @Bean
    public GroupService clcGroupService() {
        return sdk.groupService();
    }

    @Bean
    public TemplateService clcTemplateService() {
        return sdk.templateService();
    }

    @Bean
    public DataCenterService clcDataCenterService() {
        return sdk.dataCenterService();
    }

    @Bean
    public ClcSdk clcSdk() {
        return sdk;
    }

    @Bean
    public StatisticsService clcStatisticsService() {
        return sdk.statisticsService();
    }

    @Bean
    public PolicyService clcPolicyService() {
        return sdk.policyService();
    }

    @Bean
    public InvoiceService clcInvoiceService() {
        return sdk.invoiceService();
    }

    @Bean
    public LoadBalancerService clcLoadBalancerService() {
        return sdk.loadBalancerService();
    }

    @Bean
    public LoadBalancerPoolService clcLoadBalancerPoolService() {
        return sdk.loadBalancerPoolService();
    }

    @Bean
    public LoadBalancerNodeService clcLoadBalancerNodeService() {
        return sdk.loadBalancerNodeService();
    }

    @Bean
    public FirewallPolicyService clcFirewallPolicyService() {
        return sdk.firewallPolicyService();
    }

    @Bean
    public AutoscalePolicyService clcAutoscalePolicyService() {
        return sdk.autoscalePolicyService();
    }

    @Bean
    public NetworkService clcNetworkService() {
        return sdk.networkService();
    }
}
