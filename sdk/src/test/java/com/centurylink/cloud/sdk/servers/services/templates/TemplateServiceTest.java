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

package com.centurylink.cloud.sdk.servers.services.templates;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

@Test(groups = INTEGRATION)
public class TemplateServiceTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Test
    public void testFindByDataCenter() {
        List<TemplateMetadata> templates = templateService.findByDataCenter(DE_FRANKFURT.getId());

        assert templates.size() > 0;
    }

}