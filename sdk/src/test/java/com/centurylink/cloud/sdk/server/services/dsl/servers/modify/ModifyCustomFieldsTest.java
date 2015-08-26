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

package com.centurylink.cloud.sdk.server.services.dsl.servers.modify;

import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ModifyServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class ModifyCustomFieldsTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    ServerService serverService;

    ServerMetadata server;

    @Test
    @WireMockFileSource("custom-fields/create")
    public void createServer() {
        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("CSTM")
                .customFields(
                    new CustomField().name("Approved by").value("test user"),
                    new CustomField().name("Type").value("0")
                )
            )
            .waitUntilComplete()
            .getResult();
    }

    @Test
    @WireMockFileSource("custom-fields/modify")
    public void modifyServer() {
        Server serverRef = server.asRefById();

        serverService.modify(serverRef,
            new ModifyServerConfig()
                .customFields(new CustomField().name("Type").value("1"))
            )
            .waitUntilComplete()
            .getResult();

        server = serverService.findByRef(serverRef);

        List<CustomField> customFields = server.getDetails().getCustomFields();

        assert customFields.size() == 1;
        assert "1".equals(customFields.get(0).getValue());

        deleteServer();
    }

    private void deleteServer() {
        serverService.delete(server.asRefById());
    }

}
