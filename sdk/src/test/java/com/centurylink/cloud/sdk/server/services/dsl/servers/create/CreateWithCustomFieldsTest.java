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

package com.centurylink.cloud.sdk.server.services.dsl.servers.create;

import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class CreateWithCustomFieldsTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    ServerService serverService;

    ServerMetadata server;

    @Test
    @WireMockFileSource("custom-fields")
    public void testCreateServer() throws Exception {
        String approvedValue = "test user";
        String typeValue = "1";

        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("CSTM")
                .customFields(
                    new CustomField().name("Approved by").value(approvedValue),
                    new CustomField().name("Type").value(typeValue)
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(server.getId());

        server = serverService.findByRef(server.asRefById());

        List<CustomField> customFields = server.getDetails().getCustomFields();
        assert customFields.size() == 2;

        customFields.forEach(field -> {
            if ("Type".equals(field.getName())) {
                assert typeValue.equals(field.getValue());
            } else {
                assert approvedValue.equals(field.getValue());
            }
        });

    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asRefById().asFilter());
    }

}
