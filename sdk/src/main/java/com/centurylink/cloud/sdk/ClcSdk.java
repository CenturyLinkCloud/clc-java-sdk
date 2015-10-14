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

import com.centurylink.cloud.sdk.policy.services.AutoscalePolicyModule;
import com.centurylink.cloud.sdk.policy.services.dsl.AutoscalePolicyService;
import com.centurylink.cloud.sdk.base.services.BaseModule;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.CredentialsProvider;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.core.injector.SdkInjector;
import com.centurylink.cloud.sdk.policy.services.FirewallPolicyModule;
import com.centurylink.cloud.sdk.policy.services.dsl.FirewallPolicyService;
import com.centurylink.cloud.sdk.loadbalancer.services.LoadBalancerModule;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.policy.services.PolicyModule;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.server.services.NetworkModule;
import com.centurylink.cloud.sdk.server.services.ServerModule;
import com.centurylink.cloud.sdk.server.services.dsl.*;

/**
 * @author ilya.drabenia
 */
public class ClcSdk {

    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    @Inject
    TemplateService templateService;

    @Inject
    DataCenterService dataCenterService;

    @Inject
    CredentialsProvider credentialsProvider;

    @Inject
    StatisticsService statisticsService;

    @Inject
    PolicyService policyService;

    @Inject
    InvoiceService invoiceService;

    @Inject
    LoadBalancerService loadBalancerService;

    @Inject
    LoadBalancerPoolService loadBalancerPoolService;

    @Inject
    LoadBalancerNodeService loadBalancerNodeService;

    @Inject
    FirewallPolicyService firewallPolicyService;

    @Inject
    AutoscalePolicyService autoscalePolicyService;

    @Inject
    NetworkService networkService;

    public ClcSdk() {
        this(new DefaultCredentialsProvider());
    }

    public ClcSdk(CredentialsProvider credentialsProvider) {
        this(credentialsProvider, SdkConfiguration.builder().build());
    }

    public ClcSdk(CredentialsProvider credentialsProvider, SdkConfiguration config) {
        SdkInjector
            .createInjector(
                config.asModule(),
                new BaseModule(),
                new AuthModule(credentialsProvider),
                new ServerModule(),
                new PolicyModule(),
                new LoadBalancerModule(),
                new FirewallPolicyModule(),
                new AutoscalePolicyModule(),
                new NetworkModule()
            )
            .injectMembers(this);
    }

    public ServerService serverService() {
        return serverService;
    }

    public GroupService groupService() {
        return groupService;
    }

    public TemplateService templateService() {
        return templateService;
    }

    public DataCenterService dataCenterService() {
        return dataCenterService;
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public StatisticsService statisticsService() {
        return statisticsService;
    }

    public PolicyService policyService() {
        return policyService;
    }

    public InvoiceService invoiceService() {
        return invoiceService;
    }

    public LoadBalancerService loadBalancerService() {
        return loadBalancerService;
    }

    public LoadBalancerPoolService loadBalancerPoolService() {
        return loadBalancerPoolService;
    }

    public LoadBalancerNodeService loadBalancerNodeService() {
        return loadBalancerNodeService;
    }

    public FirewallPolicyService firewallPolicyService() {
        return firewallPolicyService;
    }

    public AutoscalePolicyService autoscalePolicyService() {
        return autoscalePolicyService;
    }

    public NetworkService networkService() {
        return networkService;
    }
}
