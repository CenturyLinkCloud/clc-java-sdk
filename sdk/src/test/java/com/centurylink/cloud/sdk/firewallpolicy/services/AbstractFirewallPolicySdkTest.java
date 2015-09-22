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

package com.centurylink.cloud.sdk.firewallpolicy.services;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.FirewallPolicyService;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.FirewallPolicyConfig;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.refs.FirewallPolicy;
import com.centurylink.cloud.sdk.tests.AbstractSdkTest;
import com.google.inject.Inject;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

public class AbstractFirewallPolicySdkTest extends AbstractSdkTest {

    @Inject
    protected FirewallPolicyService firewallPolicyService;

    @Inject
    protected BearerAuthentication authentication;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new FirewallPolicyModule());
    }

    protected FirewallPolicy createFirewallPolicy() {
        String destinationAccount = authentication.getAccountAlias();

        List<String> sourceList = new ArrayList<>();
        sourceList.add("10.110.5.13/32");
        sourceList.add("10.110.5.13/32");
        sourceList.add("10.110.5.13/32");

        List<String> destinationList = new ArrayList<>();
        destinationList.add("10.110.5.13/32");
        destinationList.add("10.110.5.13/32");

        List<String> portList = new ArrayList<>();
        portList.add("tcp/1-600");

        return firewallPolicyService
            .create(
                new FirewallPolicyConfig()
                    .enabled(true)
                    .destinationAccount(destinationAccount)
                    .source(sourceList)
                    .destination(destinationList)
                    .ports(portList)
                    .dataCenter(DataCenter.DE_FRANKFURT)
            )
            .waitUntilComplete()
            .getResult();
    }

}
