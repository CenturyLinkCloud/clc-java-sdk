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

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.core.injector.Module;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.ServerModule;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.DiskRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.DiskType;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.TestModule;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class DiskManagementTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    ServerClient serverClient;

    @Inject
    TemplateService templateService;

    @Inject
    GroupService groupService;

    @Inject
    DataCenterService dataCenterService;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new ServerModule(), new TestModule() {
            @Override
            protected void configure() {
                this
                    .mock(ServerClient.class)
                    .mock(TemplateService.class)
                    .mock(GroupService.class)
                    .mock(DataCenterService.class);
            }
        });
    }

    @Test
    public void testCreateServerWithDisksOperation() {
        when(templateService.findByRef(any())).thenReturn(new TemplateMetadata());

        when(groupService.findByRef(any()))
            .thenReturn(
                    new GroupMetadata() {{
                        setLocationId("de1");
                    }}
            );

        when(serverClient.findServerByUuid(any()))
            .thenReturn(
                    new ServerMetadata()
            );

        when(dataCenterService.getDeploymentCapabilities(any()))
            .thenReturn(
                new DatacenterDeploymentCapabilitiesMetadata() {{
                    setSupportsPremiumStorage(true);
                }}
            );

        when(serverClient.create(any()))
            .thenReturn(new BaseServerResponse(null, true, new ArrayList<Link>() {{
                add(new Link() {{
                    setRel("status");
                    setId("statusId");
                }});
                add(new Link() {{
                    setRel("self");
                    setId("serverId");
                }});
            }}, null));

        serverService.create(TestServerSupport.anyServerConfig()
            .machine(new Machine()
                .cpuCount(2)
                .ram(4)
                .disk(new DiskConfig()
                    .type(DiskType.RAW)
                    .size(15)
                )
                .disk(new DiskConfig()
                    .type(DiskType.PARTITIONED)
                    .path("/d1")
                    .size(20)
                )
            )
        );

        ArgumentCaptor<CreateServerRequest> request = ArgumentCaptor.forClass(CreateServerRequest.class);
        Mockito.verify(serverClient).create(request.capture());
        List<DiskRequest> additionalDisks = request.getValue().getAdditionalDisks();

        assertEquals(additionalDisks.size(), 2);
        assertEquals(additionalDisks.get(0).getType(), "raw");
        assertEquals(additionalDisks.get(0).getSizeGB(), Integer.valueOf(15));

        assertEquals(additionalDisks.get(1).getType(), "partitioned");
        assertEquals(additionalDisks.get(1).getSizeGB(), Integer.valueOf(20));
        assertEquals(additionalDisks.get(1).getPath(), "/d1");
    }

}
