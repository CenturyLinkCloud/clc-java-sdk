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

package com.centurylink.cloud.sdk.policy.services.dsl.antiaffinity.create;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.policy.services.AbstractPoliciesSdkTest;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AntiAffinityPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = RECORDED)
public class CreateAntiAffinityPolicyTest extends AbstractPoliciesSdkTest implements WireMockMixin {

    @Inject
    PolicyService policyService;

    AntiAffinityPolicy policy;

    @Test
    @WireMockFileSource
    public void testCreate() throws Exception {
        policy =
            policyService.antiAffinity().create(
                new AntiAffinityPolicyConfig()
                    .name("My policy")
                    .dataCenter(DataCenter.CA_TORONTO_1)
                )
                .waitUntilComplete()
                .getResult();

        assert !isNullOrEmpty(policyService.antiAffinity().findByRef(policy).getId());
    }

    @AfterMethod
    public void deletePolicy() {
        policyService.antiAffinity().delete(policy.asFilter());
    }

}

