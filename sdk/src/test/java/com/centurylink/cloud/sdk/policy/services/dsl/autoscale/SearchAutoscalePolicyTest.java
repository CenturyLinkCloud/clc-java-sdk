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

package com.centurylink.cloud.sdk.policy.services.dsl.autoscale;

import com.centurylink.cloud.sdk.policy.services.AbstractAutoscalePolicySdkTest;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.AutoscalePolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.filter.AutoscalePolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.refs.AutoscalePolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("search")
public class SearchAutoscalePolicyTest extends AbstractAutoscalePolicySdkTest implements WireMockMixin {

    @Test
    public void testFindAll() {

        List<AutoscalePolicyMetadata> metadataList = autoscalePolicyService.find(
                new AutoscalePolicyFilter()
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 2);
    }

    @Test
    public void testFindById() {
        String id = "02c9a0551e494c0fa6ede693268c0216";

        AutoscalePolicyMetadata autoscalePolicyMetadata = autoscalePolicyService.findByRef(
                AutoscalePolicy.refById(id)
        );

        assertNotNull(autoscalePolicyMetadata);
        assertEquals(autoscalePolicyMetadata.getId(), id);
    }

    @Test
    public void testFindByName() {
        String name = "Policy 2";

        List<AutoscalePolicyMetadata> metadataList = autoscalePolicyService.find(
                new AutoscalePolicyFilter().nameContains(name)
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 1);
        assertTrue(metadataList.get(0).getName().contains(name));
    }
}
